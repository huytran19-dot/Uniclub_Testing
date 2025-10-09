package com.uniclub.service;

import com.uniclub.dto.request.PaymentMethod.CreatePaymentMethodRequest;
import com.uniclub.dto.request.PaymentMethod.UpdatePaymentMethodRequest;
import com.uniclub.dto.response.PaymentMethod.PaymentMethodResponse;

import java.util.List;

public interface PaymentMethodService {
    List<PaymentMethodResponse> getAllPaymentMethods();
    PaymentMethodResponse getPaymentMethodById(Integer id);

    PaymentMethodResponse createPaymentMethod(CreatePaymentMethodRequest request);
    PaymentMethodResponse updatePaymentMethod(Integer id, UpdatePaymentMethodRequest request);
    void deletePaymentMethod(Integer id);
    void inactivePaymentMethod(Integer id);
}
