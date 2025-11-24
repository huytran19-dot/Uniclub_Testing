package com.uniclub.service;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TimeZone;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.uniclub.config.VNPayConfig;
import com.uniclub.entity.Order;
import com.uniclub.entity.Payment;
import com.uniclub.entity.enums.OrderStatus;
import com.uniclub.entity.enums.PaymentMethod;
import com.uniclub.entity.enums.PaymentStatus;
import com.uniclub.repository.OrderRepository;
import com.uniclub.repository.PaymentRepository;
import com.uniclub.util.VNPayUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class VNPayService {
    
    private final VNPayConfig vnPayConfig;
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    
    /**
     * Create VNPay payment URL
     */
    public String createPaymentUrl(Integer orderId, String orderInfo, String ipAddr, String locale) {
        try {
            // Get order
            Order order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));
            
            // Create payment record
            Payment payment = new Payment();
            payment.setOrder(order);
            payment.setPaymentMethod(PaymentMethod.VNPay);
            payment.setAmount(order.getTotal());
            payment.setPaymentStatus(PaymentStatus.PENDING);
            
            // Set payment expires at (15 minutes from now)
            LocalDateTime paymentExpiresAt = LocalDateTime.now().plusMinutes(15);
            payment.setPaymentExpiresAt(paymentExpiresAt);
            paymentRepository.save(payment);
            
            // Set payment expires at on order as well
            order.setPaymentExpiresAt(paymentExpiresAt);
            orderRepository.save(order);
            
            // Build VNPay params
            Map<String, String> vnpParams = new HashMap<>();
            vnpParams.put("vnp_Version", vnPayConfig.getVersion());
            vnpParams.put("vnp_Command", vnPayConfig.getCommand());
            vnpParams.put("vnp_TmnCode", vnPayConfig.getTmnCode());
            vnpParams.put("vnp_Amount", String.valueOf(order.getTotal() * 100)); // Multiply by 100
            vnpParams.put("vnp_CurrCode", "VND");
            vnpParams.put("vnp_TxnRef", String.valueOf(orderId));
            vnpParams.put("vnp_OrderInfo", orderInfo != null ? orderInfo : "Thanh toan don hang " + orderId);
            vnpParams.put("vnp_OrderType", vnPayConfig.getOrderType());
            vnpParams.put("vnp_Locale", locale != null ? locale : "vn");
            vnpParams.put("vnp_ReturnUrl", vnPayConfig.getReturnUrl());
            vnpParams.put("vnp_IpAddr", ipAddr);
            
            // Create date and expire date
            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
            String vnpCreateDate = formatter.format(calendar.getTime());
            vnpParams.put("vnp_CreateDate", vnpCreateDate);
            
            calendar.add(Calendar.MINUTE, 15); // Expire after 15 minutes
            String vnpExpireDate = formatter.format(calendar.getTime());
            vnpParams.put("vnp_ExpireDate", vnpExpireDate);
            
            // Build query string
            String queryString = VNPayUtil.buildQueryString(vnpParams);
            
            // Generate secure hash
            String secureHash = VNPayUtil.hmacSHA512(vnPayConfig.getHashSecret(), queryString);
            
            // Build final URL
            String paymentUrl = vnPayConfig.getVnpayUrl() + "?" + queryString + "&vnp_SecureHash=" + secureHash;
            
            log.info("Created VNPay payment URL for order: {} with expiration at: {}", orderId, paymentExpiresAt);
            return paymentUrl;
            
        } catch (Exception e) {
            log.error("Error creating VNPay payment URL", e);
            throw new RuntimeException("Error creating payment URL: " + e.getMessage());
        }
    }
    
    /**
     * Handle VNPay IPN (Instant Payment Notification)
     * This is called by VNPay server to notify payment result
     */
    @Transactional
    public Map<String, String> handleIPN(Map<String, String> vnpParams) {
        Map<String, String> response = new HashMap<>();
        
        try {
            // Validate secure hash
            String vnpSecureHash = vnpParams.get("vnp_SecureHash");
            if (!VNPayUtil.validateSecureHash(vnpParams, vnPayConfig.getHashSecret(), vnpSecureHash)) {
                log.error("Invalid secure hash from VNPay IPN");
                response.put("RspCode", "97");
                response.put("Message", "Invalid signature");
                return response;
            }
            
            // Extract data
            String orderId = vnpParams.get("vnp_TxnRef");
            String transactionNo = vnpParams.get("vnp_TransactionNo");
            String responseCode = vnpParams.get("vnp_ResponseCode");
            String transactionStatus = vnpParams.get("vnp_TransactionStatus");
            String bankCode = vnpParams.get("vnp_BankCode");
            String amountStr = vnpParams.get("vnp_Amount");
            
            // Find order
            Optional<Order> orderOpt = orderRepository.findById(Integer.valueOf(orderId));
            if (orderOpt.isEmpty()) {
                log.error("Order not found: {}", orderId);
                response.put("RspCode", "01");
                response.put("Message", "Order not found");
                return response;
            }
            
            Order order = orderOpt.get();
            
            // Validate amount
            Integer vnpAmount = Integer.valueOf(amountStr) / 100; // Divide by 100
            if (!order.getTotal().equals(vnpAmount)) {
                log.error("Invalid amount. Order total: {}, VNPay amount: {}", order.getTotal(), vnpAmount);
                response.put("RspCode", "04");
                response.put("Message", "Invalid amount");
                return response;
            }
            
            // Find payment record
            List<Payment> payments = paymentRepository.findByOrderId(order.getId());
            Payment payment = payments.stream()
                    .filter(p -> p.getPaymentMethod() == PaymentMethod.VNPay && p.getPaymentStatus() == PaymentStatus.PENDING)
                    .findFirst()
                    .orElse(null);
            
            if (payment == null) {
                log.error("Payment record not found for order: {}", orderId);
                response.put("RspCode", "01");
                response.put("Message", "Payment record not found");
                return response;
            }
            
            // Check if already processed
            if (payment.getPaymentStatus() != PaymentStatus.PENDING) {
                log.warn("Payment already processed for order: {}", orderId);
                response.put("RspCode", "02");
                response.put("Message", "Order already confirmed");
                return response;
            }
            
            // Update payment status
            payment.setTransactionNo(transactionNo);
            payment.setVnpayBankCode(bankCode);
            payment.setVnpayResponseCode(responseCode);
            
            if ("00".equals(responseCode) && "00".equals(transactionStatus)) {
                // Payment success
                payment.setPaymentStatus(PaymentStatus.SUCCESS);
                payment.setPaidAt(LocalDateTime.now());
                
                // Update order status to CONFIRMED
                order.setStatus(OrderStatus.CONFIRMED);
                
                log.info("Payment successful for order: {}", orderId);
            } else {
                // Payment failed
                payment.setPaymentStatus(PaymentStatus.FAILED);
                log.warn("Payment failed for order: {}. Response code: {}", orderId, responseCode);
            }
            
            paymentRepository.save(payment);
            orderRepository.save(order);
            
            response.put("RspCode", "00");
            response.put("Message", "Confirm Success");
            
        } catch (Exception e) {
            log.error("Error handling VNPay IPN", e);
            response.put("RspCode", "99");
            response.put("Message", "Unknown error");
        }
        
        return response;
    }
    
    /**
     * Handle VNPay return URL
     * This is where user is redirected after payment
     * NOTE: This also updates order status for local/demo environments where IPN cannot be called
     */
    @Transactional
    public Map<String, Object> handleReturn(Map<String, String> vnpParams) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // Validate secure hash
            String vnpSecureHash = vnpParams.get("vnp_SecureHash");
            if (!VNPayUtil.validateSecureHash(vnpParams, vnPayConfig.getHashSecret(), vnpSecureHash)) {
                log.error("Invalid secure hash from VNPay return");
                result.put("success", false);
                result.put("message", "Chữ ký không hợp lệ");
                return result;
            }
            
            String responseCode = vnpParams.get("vnp_ResponseCode");
            String transactionStatus = vnpParams.get("vnp_TransactionStatus");
            String orderId = vnpParams.get("vnp_TxnRef");
            String transactionNo = vnpParams.get("vnp_TransactionNo");
            String bankCode = vnpParams.get("vnp_BankCode");
            
            result.put("orderId", orderId);
            result.put("transactionNo", transactionNo);
            
            // Update order status (important for local/demo environments)
            try {
                Optional<Order> orderOpt = orderRepository.findById(Integer.valueOf(orderId));
                if (orderOpt.isPresent()) {
                    Order order = orderOpt.get();
                    
                    // Find payment record
                    List<Payment> payments = paymentRepository.findByOrderId(order.getId());
                    Payment payment = payments.stream()
                            .filter(p -> p.getPaymentMethod() == PaymentMethod.VNPay)
                            .findFirst()
                            .orElse(null);
                    
                    if (payment != null && payment.getPaymentStatus() == PaymentStatus.PENDING) {
                        // Update payment
                        payment.setTransactionNo(transactionNo);
                        payment.setVnpayBankCode(bankCode);
                        payment.setVnpayResponseCode(responseCode);
                        
                        if ("00".equals(responseCode) && "00".equals(transactionStatus)) {
                            // Payment success
                            payment.setPaymentStatus(PaymentStatus.SUCCESS);
                            payment.setPaidAt(LocalDateTime.now());
                            
                            // Update order status to CONFIRMED
                            order.setStatus(OrderStatus.CONFIRMED);
                            
                            log.info("Payment successful via return URL for order: {}", orderId);
                        } else {
                            // Payment failed
                            payment.setPaymentStatus(PaymentStatus.FAILED);
                            log.warn("Payment failed via return URL for order: {}. Response code: {}", orderId, responseCode);
                        }
                        
                        paymentRepository.save(payment);
                        orderRepository.save(order);
                    }
                }
            } catch (Exception e) {
                log.error("Error updating order status from return URL", e);
                // Continue to return result to user even if update fails
            }
            
            if ("00".equals(responseCode)) {
                result.put("success", true);
                result.put("message", "Giao dịch thành công");
            } else {
                result.put("success", false);
                result.put("message", "Giao dịch không thành công");
            }
            
            result.put("responseCode", responseCode);
            
        } catch (Exception e) {
            log.error("Error handling VNPay return", e);
            result.put("success", false);
            result.put("message", "Lỗi xử lý kết quả thanh toán");
        }
        
        return result;
    }
}
