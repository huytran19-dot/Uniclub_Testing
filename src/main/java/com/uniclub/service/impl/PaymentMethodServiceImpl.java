package com.uniclub.service.impl;

import com.uniclub.dto.request.PaymentMethod.CreatePaymentMethodRequest;
import com.uniclub.dto.request.PaymentMethod.UpdatePaymentMethodRequest;
import com.uniclub.dto.response.PaymentMethod.PaymentMethodResponse;
import com.uniclub.entity.PaymentMethod;
import com.uniclub.exception.ResourceNotFoundException;
import com.uniclub.repository.PaymentMethodRepository;
import com.uniclub.service.PaymentMethodService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class PaymentMethodServiceImpl implements PaymentMethodService {

    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    @Override
    public List<PaymentMethodResponse> getAllPaymentMethods() {
        return paymentMethodRepository.findAll().stream()
                .map(PaymentMethodResponse::fromEntity)
                .toList();
    }

    @Override
    public PaymentMethodResponse getPaymentMethodById(Integer id) {
        PaymentMethod paymentMethod = paymentMethodRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PaymentMethod", "id", id));
        return PaymentMethodResponse.fromEntity(paymentMethod);
    }

    @Override
    public PaymentMethodResponse createPaymentMethod(CreatePaymentMethodRequest request) {
        // Kiểm tra trùng tên phương thức thanh toán
        if (paymentMethodRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("Payment method name already exists");
        }

        PaymentMethod paymentMethod = new PaymentMethod();
        paymentMethod.setName(request.getName());
        paymentMethod.setDescription(request.getDescription());

        PaymentMethod saved = paymentMethodRepository.save(paymentMethod);
        return PaymentMethodResponse.fromEntity(saved);
    }

    @Override
    public PaymentMethodResponse updatePaymentMethod(Integer id, UpdatePaymentMethodRequest request) {
        PaymentMethod paymentMethod = paymentMethodRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PaymentMethod", "id", id));

        // Kiểm tra nếu đổi tên trùng với phương thức khác
        if (request.getName() != null && !request.getName().equals(paymentMethod.getName())) {
            if (paymentMethodRepository.existsByName(request.getName())) {
                throw new IllegalArgumentException("Payment method name already exists");
            }
            paymentMethod.setName(request.getName());
        }

        if (request.getDescription() != null) {
            paymentMethod.setDescription(request.getDescription());
        }

        if (request.getStatus() != null) {
            paymentMethod.setStatus(request.getStatus());
        }

        PaymentMethod updated = paymentMethodRepository.save(paymentMethod);
        return PaymentMethodResponse.fromEntity(updated);
    }

    @Override
    public void deletePaymentMethod(Integer id) {
        if (!paymentMethodRepository.existsById(id)) {
            throw new ResourceNotFoundException("PaymentMethod", "id", id);
        }
        paymentMethodRepository.deleteById(id);
    }

    @Override
    public void inactivePaymentMethod(Integer id) {
        PaymentMethod paymentMethod = paymentMethodRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PaymentMethod", "id", id));

        paymentMethod.setStatus((byte) 0);
        paymentMethodRepository.save(paymentMethod);
    }
}
