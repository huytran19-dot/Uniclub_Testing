package com.uniclub.dto.request.Order;

import com.uniclub.entity.enums.OrderStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class CreateOrderRequest {
    @Size(max = 255, message = "Note cannot exceed 255 characters")
    private String note;

    @NotNull(message = "User ID is required")
    private Integer userId;

    @NotNull(message = "Order items cannot be empty")
    private List<CreateOrderVariantRequest> orderVariants;
    
    private OrderStatus status;
}
