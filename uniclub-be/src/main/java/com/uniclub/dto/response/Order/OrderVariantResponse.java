package com.uniclub.dto.response.Order;

import com.uniclub.entity.OrderVariant;

import lombok.Data;

@Data
public class OrderVariantResponse {
    private Integer variantSku;
    private String productName;
    private String sizeName;
    private String colorName;
    private String images;
    private Integer quantity;
    private Integer price;

    public static OrderVariantResponse fromEntity(OrderVariant ov) {
        var variant = ov.getVariant();

        OrderVariantResponse res = new OrderVariantResponse();
        res.setVariantSku(variant.getSku());
        // Lấy tên product
        if (variant.getProduct() != null) {
            res.setProductName(variant.getProduct().getName());
        }

        // Lấy tên size
        if (variant.getSize() != null) {
            res.setSizeName(variant.getSize().getName());
        }

        // Lấy tên color
        if (variant.getColor() != null) {
            res.setColorName(variant.getColor().getName());
        }

        res.setImages(variant.getImages());
        res.setQuantity(ov.getQuantity());
        res.setPrice(ov.getPrice());
        return res;
    }
}
