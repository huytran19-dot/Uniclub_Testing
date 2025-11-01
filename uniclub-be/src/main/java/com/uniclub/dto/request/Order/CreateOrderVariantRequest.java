package com.uniclub.dto.request.Order;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateOrderVariantRequest {
    @NotNull(message = "Variant ID is required")
    private Integer variantSku; // mapping tới Variant.sku

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    @NotNull(message = "Price is required")
    @Min(value = 0, message = "Price cannot be negative")
    private Integer price;
}