package com.uniclub.dto.request.CartItem;

import lombok.Data;
import jakarta.validation.constraints.Positive;

@Data
public class UpdateCartItemRequest {
    @Positive(message = "Số lượng phải lớn hơn 0")
    private Integer quantity;

    @Positive(message = "Giá đơn vị phải lớn hơn 0")
    private Integer unitPrice;
}
