package com.uniclub.dto.response.Variant;

import com.uniclub.entity.Variant;
import lombok.Data;


import java.time.LocalDateTime;

@Data
public class VariantResponse {
    private Integer sku;
    private Integer productId;
    private Integer colorId;
    private Integer sizeId;
    private String images;
    private Integer quantity;
    private Integer price;
    private Byte status;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;


    public static VariantResponse fromEntity(Variant variant) {
        VariantResponse response = new VariantResponse();
        response.setSku(variant.getSku());
        response.setProductId(variant.getProduct() != null ? variant.getProduct().getId() : null);
        response.setColorId(variant.getColor() != null ? variant.getColor().getId() : null);
        response.setSizeId(variant.getSize() != null ? variant.getSize().getId() : null);
        response.setImages(variant.getImages());
        response.setQuantity(variant.getQuantity());
        response.setPrice(variant.getPrice());
        response.setStatus(variant.getStatus());
        response.setCreated_at(variant.getCreatedAt());
        response.setUpdated_at(variant.getUpdatedAt());

        return response;
    }
}
