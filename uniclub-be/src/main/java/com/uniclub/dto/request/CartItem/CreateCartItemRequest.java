package com.uniclub.dto.request.CartItem;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Data
public class CreateCartItemRequest {
    @NotNull(message = "ID giỏ hàng không được để trống")
    private Integer cartId;

    @NotNull(message = "SKU sản phẩm không được để trống")
    private Integer variantSku;

    @NotNull(message = "Số lượng không được để trống")
    @Positive(message = "Số lượng phải lớn hơn 0")
    private Integer quantity;

    @NotNull(message = "Giá đơn vị không được để trống")
    @Positive(message = "Giá đơn vị phải lớn hơn 0")
    private Integer unitPrice;
}
