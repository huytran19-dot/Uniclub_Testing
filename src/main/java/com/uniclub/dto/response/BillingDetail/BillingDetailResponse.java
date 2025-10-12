package com.uniclub.dto.response.BillingDetail;

import lombok.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.uniclub.entity.BillingDetail;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BillingDetailResponse {

    private Integer id;
    private Integer orderId;
    private String fullName;
    private String phone;
    private String email;
    private String address;
    private String province;
    private String district;
    private String ward;
    private String note;
    private Integer status;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    //Hàm chuyển từ Entity sang DTO Response
    public static BillingDetailResponse fromEntity(BillingDetail billingDetail) {
        if (billingDetail == null) return null;
        return BillingDetailResponse.builder()
                .id(billingDetail.getId())
                .orderId(billingDetail.getOrder() != null ? billingDetail.getOrder().getId() : null)
                .fullName(billingDetail.getFullName())
                .phone(billingDetail.getPhone())
                .email(billingDetail.getEmail())
                .address(billingDetail.getAddress())
                .province(billingDetail.getProvince())
                .district(billingDetail.getDistrict())
                .ward(billingDetail.getWard())
                .note(billingDetail.getNote())
                .status(billingDetail.getStatus())
                .createdAt(billingDetail.getCreatedAt())
                .updatedAt(billingDetail.getUpdatedAt())
                .build();
    }
}
