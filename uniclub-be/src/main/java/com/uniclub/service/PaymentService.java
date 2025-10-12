package com.uniclub.service;

import com.uniclub.dto.request.Payment.CreatePaymentRequest;
import com.uniclub.dto.request.Payment.UpdatePaymentRequest;
import com.uniclub.dto.response.Payment.PaymentResponse;

import java.util.List;

public interface PaymentService {
    PaymentResponse createPayment(CreatePaymentRequest request);
    PaymentResponse updatePayment(Integer paymentId, UpdatePaymentRequest request);
    void deletePayment(Integer id);

    List<PaymentResponse> getAllPayments();
    PaymentResponse getPaymentById(Integer id);
    List<PaymentResponse> getPaymentsByOrderId(Integer orderId);
    List<PaymentResponse> getPaymentsByStatus(String status);
    PaymentResponse processPayment(Integer paymentId);
}
