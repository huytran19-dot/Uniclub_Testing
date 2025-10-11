package com.uniclub.dto.response.CartItem;

import lombok.*;
import java.time.LocalDateTime;
import com.uniclub.entity.CartItem;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemResponse {

    private Integer id;
    private Integer quantity;
    private Integer unitPrice;
    private Integer subtotal;
    private LocalDateTime addedAt;
    private Integer cartId;
    private Integer variantSku;

    //Hàm chuyển từ Entity sang DTO Response
    public static CartItemResponse fromEntity(CartItem cartItem) {
        if (cartItem == null) return null;
        return CartItemResponse.builder()
                .id(cartItem.getId())
                .quantity(cartItem.getQuantity())
                .unitPrice(cartItem.getUnitPrice())
                .subtotal(cartItem.getSubtotal())
                .addedAt(cartItem.getAddedAt())
                .cartId(cartItem.getCart() != null ? cartItem.getCart().getId() : null)
                .variantSku(cartItem.getVariant() != null ? cartItem.getVariant().getSku() : null)
                .build();
    }
}
