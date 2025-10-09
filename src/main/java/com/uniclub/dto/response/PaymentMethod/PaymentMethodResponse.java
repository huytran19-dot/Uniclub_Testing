package com.uniclub.dto.response.PaymentMethod;

import com.uniclub.entity.PaymentMethod;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PaymentMethodResponse {
    private Integer id;
    private String name;
    private String description;
    private Byte status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static PaymentMethodResponse fromEntity(PaymentMethod paymentMethod) {
        PaymentMethodResponse response = new PaymentMethodResponse();
        response.setId(paymentMethod.getId());
        response.setName(paymentMethod.getName());
        response.setDescription(paymentMethod.getDescription());
        response.setStatus(paymentMethod.getStatus());
        response.setCreatedAt(paymentMethod.getCreatedAt());
        response.setUpdatedAt(paymentMethod.getUpdatedAt());

        return response;
    }
}
