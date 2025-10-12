package com.uniclub.dto.response.Cart;

import lombok.*;
import java.time.LocalDateTime;
import com.uniclub.entity.Cart;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartResponse {

    private Integer id;
    private Integer totalPrice;
    private Integer shippingFee;
    private String note;
    private Byte status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer userId;

    //Hàm chuyển từ Entity sang DTO Response
    public static CartResponse fromEntity(Cart cart) {
        if (cart == null) return null;
        return CartResponse.builder()
                .id(cart.getId())
                .totalPrice(cart.getTotalPrice())
                .shippingFee(cart.getShippingFee())
                .note(cart.getNote())
                .status(cart.getStatus())
                .createdAt(cart.getCreatedAt())
                .updatedAt(cart.getUpdatedAt())
                .userId(cart.getUser() != null ? cart.getUser().getId() : null)
                .build();
    }
}
