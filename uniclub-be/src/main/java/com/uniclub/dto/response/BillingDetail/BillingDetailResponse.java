package com.uniclub.dto.response.BillingDetail;

import lombok.*;
import java.util.Date;
import com.uniclub.entity.BillingDetail;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BillingDetailResponse {

    private Integer id;
    private String firstName;
    private String lastName;
    private String companyName;
    private String address;
    private String town;
    private String state;
    private Integer zipCode;
    private String phone;
    private String email;
    private Date createDate;

    //Hàm chuyển từ Entity sang DTO Response
    public static BillingDetailResponse fromEntity(BillingDetail billingDetail) {
        if (billingDetail == null) return null;
        return BillingDetailResponse.builder()
                .id(billingDetail.getId())
                .firstName(billingDetail.getFirstName())
                .lastName(billingDetail.getLastName())
                .companyName(billingDetail.getCompanyName())
                .address(billingDetail.getAddress())
                .town(billingDetail.getTown())
                .state(billingDetail.getState())
                .zipCode(billingDetail.getZipCode())
                .phone(billingDetail.getPhone())
                .email(billingDetail.getEmail())
                .createDate(billingDetail.getCreateDate())
                .build();
    }
}
