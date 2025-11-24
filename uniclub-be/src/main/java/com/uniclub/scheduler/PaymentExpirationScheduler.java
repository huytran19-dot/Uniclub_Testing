package com.uniclub.scheduler;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.uniclub.entity.Order;
import com.uniclub.entity.Payment;
import com.uniclub.entity.Variant;
import com.uniclub.entity.enums.OrderStatus;
import com.uniclub.entity.enums.PaymentMethod;
import com.uniclub.entity.enums.PaymentStatus;
import com.uniclub.repository.OrderRepository;
import com.uniclub.repository.PaymentRepository;
import com.uniclub.repository.VariantRepository;

import lombok.extern.slf4j.Slf4j;

/**
 * Scheduler để kiểm tra và hủy các đơn hàng VNPay vượt quá thời gian thanh toán
 * Chạy mỗi 5 phút để kiểm tra các payment đã hết hạn
 */
@Slf4j
@Component
public class PaymentExpirationScheduler {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PaymentRepository paymentRepository;
    
    @Autowired
    private VariantRepository variantRepository;

    /**
     * Kiểm tra mỗi 5 phút (300000ms) xem có payment VNPay nào hết hạn không
     * Nếu hết hạn, set order status = CANCELLED và payment status = FAILED
     */
    @Scheduled(fixedDelay = 300000, initialDelay = 60000) // 5 phút, khởi động sau 1 phút
    @Transactional
    public void checkExpiredPayments() {
        try {
            log.info("=== Starting payment expiration check ===");
            
            LocalDateTime now = LocalDateTime.now();
            
            // Tìm tất cả đơn hàng PENDING với VNPay payment chưa thành công
            List<Order> pendingOrders = orderRepository.findByStatus(OrderStatus.PENDING);
            
            for (Order order : pendingOrders) {
                // Chỉ kiểm tra những đơn dùng VNPay
                var payment = paymentRepository.findLatestByOrderId(order.getId());
                
                if (payment.isPresent() && 
                    payment.get().getPaymentMethod() == PaymentMethod.VNPay &&
                    payment.get().getPaymentStatus() != PaymentStatus.SUCCESS) {
                    
                    // Nếu payment_expires_at đã qua
                    if (order.getPaymentExpiresAt() != null && order.getPaymentExpiresAt().isBefore(now)) {
                        log.warn("Payment expired for order {}, cancelling...", order.getId());
                        
                        // Update order status to CANCELLED
                        order.setStatus(OrderStatus.CANCELLED);
                        orderRepository.save(order);
                        
                        // Update payment status to FAILED
                        Payment failedPayment = payment.get();
                        failedPayment.setPaymentStatus(PaymentStatus.FAILED);
                        paymentRepository.save(failedPayment);
                        
                        // Restore variant quantities (hoàn lại tồn kho)
                        for (var orderVariant : order.getOrderVariants()) {
                            Variant variant = orderVariant.getVariant();
                            variant.setQuantity(variant.getQuantity() + orderVariant.getQuantity());
                            variantRepository.save(variant);
                            log.info("Restored {} units to variant {}", orderVariant.getQuantity(), variant.getSku());
                        }
                        
                        log.info("Order {} and Payment {} marked as expired and inventory restored", order.getId(), failedPayment.getId());
                    }
                }
            }
            
            log.info("=== Payment expiration check completed ===");
        } catch (Exception e) {
            log.error("Error during payment expiration check", e);
        }
    }
}
