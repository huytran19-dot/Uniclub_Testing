package com.uniclub.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.uniclub.entity.Payment;
import com.uniclub.entity.enums.PaymentMethod;
import com.uniclub.entity.enums.PaymentStatus;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    List<Payment> findByOrderId(Integer orderId);
    List<Payment> findByPaymentMethod(PaymentMethod paymentMethod);
    List<Payment> findByPaymentStatus(PaymentStatus paymentStatus);
    List<Payment> findByTransactionNo(String transactionNo);
    boolean existsByTransactionNo(String transactionNo);
    
    // Get latest payment for an order (sorted by created_at descending)
    @Query("SELECT p FROM Payment p WHERE p.order.id = ?1 ORDER BY p.createdAt DESC LIMIT 1")
    Optional<Payment> findLatestByOrderId(Integer orderId);
}

