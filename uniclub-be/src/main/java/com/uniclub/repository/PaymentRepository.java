package com.uniclub.repository;

import com.uniclub.entity.Payment;
import com.uniclub.entity.enums.PaymentMethod;
import com.uniclub.entity.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    List<Payment> findByOrderId(Integer orderId);
    List<Payment> findByPaymentMethod(PaymentMethod paymentMethod);
    List<Payment> findByPaymentStatus(PaymentStatus paymentStatus);
    List<Payment> findByTransactionNo(String transactionNo);
    boolean existsByTransactionNo(String transactionNo);
}
