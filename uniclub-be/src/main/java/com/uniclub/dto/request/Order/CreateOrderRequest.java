package com.uniclub.dto.request.Order;

import java.util.List;

import com.uniclub.entity.enums.OrderStatus;
import com.uniclub.entity.enums.PaymentMethod;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateOrderRequest {
    @Size(max = 255, message = "Note cannot exceed 255 characters")
    private String note;

    @NotNull(message = "User ID is required")
    private Integer userId;

    @NotNull(message = "Order items cannot be empty")
    private List<CreateOrderVariantRequest> orderVariants;
    
    private OrderStatus status;
    
    // Shipping information
    @NotBlank(message = "Recipient name is required")
    @Size(max = 100, message = "Recipient name cannot exceed 100 characters")
    private String recipientName;
    
    @NotBlank(message = "Recipient phone is required")
    @Size(max = 20, message = "Phone number cannot exceed 20 characters")
    private String recipientPhone;
    
    @NotBlank(message = "Shipping address is required")
    @Size(max = 500, message = "Shipping address cannot exceed 500 characters")
    private String shippingAddress;
    
    // Payment method
    @NotNull(message = "Payment method is required")
    private PaymentMethod paymentMethod;
}
