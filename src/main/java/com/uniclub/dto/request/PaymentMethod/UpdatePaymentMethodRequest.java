package com.uniclub.dto.request.PaymentMethod;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdatePaymentMethodRequest {
    @Size(max = 50, message = "PaymentMethod name cannot exceed 50 characters")
    private String name;

    @Size(max = 255, message = "Description cannot exceed 255 characters")
    private String description;

    private Byte status;
}
