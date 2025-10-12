package com.uniclub.dto.response.Brand;

import lombok.*;
import java.time.LocalDateTime;
import com.uniclub.entity.Brand;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BrandResponse {

    private Integer id;
    private String name;
    private Byte status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    //Hàm  chuyển từ Entity sang DTO Response
    public static BrandResponse fromEntity(Brand brand) {
        if (brand == null) return null;
        return BrandResponse.builder()
                .id(brand.getId())
                .name(brand.getName())
                .status(brand.getStatus())
                .createdAt(brand.getCreatedAt())
                .updatedAt(brand.getUpdatedAt())
                .build();
    }
}
