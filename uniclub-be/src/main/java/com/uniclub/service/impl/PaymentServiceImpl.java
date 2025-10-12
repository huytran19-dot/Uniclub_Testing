package com.uniclub.service.impl;

import com.uniclub.dto.request.Payment.CreatePaymentRequest;
import com.uniclub.dto.request.Payment.UpdatePaymentRequest;
import com.uniclub.dto.response.Payment.PaymentResponse;
import com.uniclub.entity.Order;
import com.uniclub.entity.Payment;
import com.uniclub.entity.enums.PaymentStatus;
import com.uniclub.exception.ResourceNotFoundException;
import com.uniclub.repository.OrderRepository;
import com.uniclub.repository.PaymentRepository;
import com.uniclub.service.PaymentService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    private PaymentRepository paymentRepository;
    
    @Autowired
    private OrderRepository orderRepository;

    @Override
    public PaymentResponse createPayment(CreatePaymentRequest request) {
        // Check if order exists
        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", request.getOrderId()));

        // Check if transaction number already exists (if provided)
        if (request.getTransactionNo() != null && !request.getTransactionNo().trim().isEmpty()) {
            if (paymentRepository.existsByTransactionNo(request.getTransactionNo())) {
                throw new IllegalArgumentException("Mã giao dịch đã tồn tại");
            }
        }

        Payment payment = new Payment();
        payment.setPaymentMethod(request.getPaymentMethod());
        payment.setTransactionNo(request.getTransactionNo());
        payment.setAmount(request.getAmount());
        payment.setOrder(order);
        payment.setPaymentStatus(PaymentStatus.PENDING);

        Payment savedPayment = paymentRepository.save(payment);
        return PaymentResponse.fromEntity(savedPayment);
    }

    @Override
    public PaymentResponse updatePayment(Integer paymentId, UpdatePaymentRequest request) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment", "id", paymentId));

        // Check if transaction number is being changed and if it already exists
        if (request.getTransactionNo() != null && !request.getTransactionNo().equals(payment.getTransactionNo())) {
            if (paymentRepository.existsByTransactionNo(request.getTransactionNo())) {
                throw new IllegalArgumentException("Mã giao dịch đã tồn tại");
            }
            payment.setTransactionNo(request.getTransactionNo());
        }

        if (request.getAmount() != null) {
            payment.setAmount(request.getAmount());
        }
        if (request.getPaymentStatus() != null) {
            payment.setPaymentStatus(request.getPaymentStatus());
            // If status is SUCCESS, set paidAt timestamp
            if (request.getPaymentStatus() == PaymentStatus.SUCCESS) {
                payment.setPaidAt(LocalDateTime.now());
            }
        }

        Payment updatedPayment = paymentRepository.save(payment);
        return PaymentResponse.fromEntity(updatedPayment);
    }

    @Override
    public List<PaymentResponse> getAllPayments() {
        return paymentRepository.findAll()
                .stream()
                .map(PaymentResponse::fromEntity)
                .toList();
    }

    @Override
    public PaymentResponse getPaymentById(Integer paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment", "id", paymentId));
        return PaymentResponse.fromEntity(payment);
    }

    @Override
    public List<PaymentResponse> getPaymentsByOrderId(Integer orderId) {
        return paymentRepository.findByOrderId(orderId)
                .stream()
                .map(PaymentResponse::fromEntity)
                .toList();
    }

    @Override
    public List<PaymentResponse> getPaymentsByStatus(String status) {
        try {
            PaymentStatus paymentStatus = PaymentStatus.valueOf(status.toUpperCase());
            return paymentRepository.findByPaymentStatus(paymentStatus)
                    .stream()
                    .map(PaymentResponse::fromEntity)
                    .toList();
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Trạng thái thanh toán không hợp lệ: " + status);
        }
    }

    @Override
    public PaymentResponse processPayment(Integer paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment", "id", paymentId));

        // Simulate payment processing logic
        if (payment.getPaymentStatus() == PaymentStatus.PENDING) {
            // In real implementation, this would integrate with payment gateway
            payment.setPaymentStatus(PaymentStatus.SUCCESS);
            payment.setPaidAt(LocalDateTime.now());
            
            // Generate transaction number if not exists
            if (payment.getTransactionNo() == null || payment.getTransactionNo().trim().isEmpty()) {
                payment.setTransactionNo("TXN" + System.currentTimeMillis());
            }
        } else {
            throw new IllegalArgumentException("Chỉ có thể xử lý thanh toán đang chờ");
        }

        Payment processedPayment = paymentRepository.save(payment);
        return PaymentResponse.fromEntity(processedPayment);
    }

    @Override
    public void deletePayment(Integer paymentId) {
        if (!paymentRepository.existsById(paymentId)) {
            throw new ResourceNotFoundException("Payment", "id", paymentId);
        }
        paymentRepository.deleteById(paymentId);
    }
}
