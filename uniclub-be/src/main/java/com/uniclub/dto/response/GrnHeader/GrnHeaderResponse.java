package com.uniclub.dto.response.GrnHeader;

import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import com.uniclub.entity.GrnHeader;
import com.uniclub.entity.enums.GrnStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GrnHeaderResponse {

    private Integer id;
    private Integer totalCost;
    private String note;
    private LocalDate receivedDate;
    private GrnStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer supplierId;
    private String supplierName;

    //Hàm chuyển từ Entity sang DTO Response
    public static GrnHeaderResponse fromEntity(GrnHeader grnHeader) {
        if (grnHeader == null) return null;
        return GrnHeaderResponse.builder()
                .id(grnHeader.getId())
                .totalCost(grnHeader.getTotalCost())
                .note(grnHeader.getNote())
                .receivedDate(grnHeader.getReceivedDate())
                .status(grnHeader.getStatus())
                .createdAt(grnHeader.getCreatedAt())
                .updatedAt(grnHeader.getUpdatedAt())
                .supplierId(grnHeader.getSupplier() != null ? grnHeader.getSupplier().getId() : null)
                .supplierName(grnHeader.getSupplier() != null ? grnHeader.getSupplier().getName() : null)
                .build();
    }
}
