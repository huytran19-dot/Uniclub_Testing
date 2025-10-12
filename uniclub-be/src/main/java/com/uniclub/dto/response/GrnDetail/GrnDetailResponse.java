package com.uniclub.dto.response.GrnDetail;

import lombok.*;
import com.uniclub.entity.GrnDetail;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GrnDetailResponse {

    private Integer id;
    private Integer quantity;
    private Integer unitCost;
    private Integer subtotal;
    private Integer grnHeaderId;
    private Integer variantSku;
    private String variantName;
    private String productName;

    //Hàm chuyển từ Entity sang DTO Response
    public static GrnDetailResponse fromEntity(GrnDetail grnDetail) {
        if (grnDetail == null) return null;
        return GrnDetailResponse.builder()
                .id(grnDetail.getId())
                .quantity(grnDetail.getQuantity())
                .unitCost(grnDetail.getUnitCost())
                .subtotal(grnDetail.getSubtotal())
                .grnHeaderId(grnDetail.getGrnHeader() != null ? grnDetail.getGrnHeader().getId() : null)
                .variantSku(grnDetail.getVariant() != null ? grnDetail.getVariant().getSku() : null)
                .variantName(grnDetail.getVariant() != null && grnDetail.getVariant().getProduct() != null 
                    ? grnDetail.getVariant().getProduct().getName() : null)
                .productName(grnDetail.getVariant() != null && grnDetail.getVariant().getProduct() != null 
                    ? grnDetail.getVariant().getProduct().getName() : null)
                .build();
    }
}
