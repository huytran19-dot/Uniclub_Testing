package com.uniclub.dto.response.Product;

import com.uniclub.dto.response.Variant.VariantResponse;
import com.uniclub.entity.Product;
import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponse {

    private Integer id;
    private String name;
    private String description;
    private String information;
    private Integer price;

    private Integer idBrand;
    private String brandName;

    private Integer idCategory;
    private String categoryName;

    private Byte status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ProductResponse fromEntity(Product product) {
        if (product == null) return null;

        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setInformation(product.getInformation());
        response.setPrice(product.getPrice());
        response.setStatus(product.getStatus());
        response.setCreatedAt(product.getCreatedAt());
        response.setUpdatedAt(product.getUpdatedAt());

        // lay brandname
        if (product.getBrand() != null) {
            response.setIdBrand(product.getBrand().getId());
        }

        // lay categoryName
        if (product.getCategory() != null) {
            response.setIdCategory(product.getCategory().getId());
        }

        return response;
    }
}
