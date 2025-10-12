package com.uniclub.dto.response.Supplier;

import lombok.*;
import java.time.LocalDateTime;
import com.uniclub.entity.Supplier;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupplierResponse {

    private Integer id;
    private String name;
    private String contactPerson;
    private String phone;
    private String email;
    private String address;
    private Byte status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    //Hàm chuyển từ Entity sang DTO Response
    public static SupplierResponse fromEntity(Supplier supplier) {
        if (supplier == null) return null;
        return SupplierResponse.builder()
                .id(supplier.getId())
                .name(supplier.getName())
                .contactPerson(supplier.getContactPerson())
                .phone(supplier.getPhone())
                .email(supplier.getEmail())
                .address(supplier.getAddress())
                .status(supplier.getStatus())
                .createdAt(supplier.getCreatedAt())
                .updatedAt(supplier.getUpdatedAt())
                .build();
    }
}
