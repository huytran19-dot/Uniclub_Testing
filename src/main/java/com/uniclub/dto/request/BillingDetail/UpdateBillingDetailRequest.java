package com.uniclub.dto.request.BillingDetail;

import lombok.Data;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Data
public class UpdateBillingDetailRequest {
    @Size(max = 50, message = "Tên không được vượt quá 50 ký tự")
    private String firstName;

    @Size(max = 50, message = "Họ không được vượt quá 50 ký tự")
    private String lastName;

    @Size(max = 100, message = "Tên công ty không được vượt quá 100 ký tự")
    private String companyName;

    @Size(max = 255, message = "Địa chỉ không được vượt quá 255 ký tự")
    private String address;

    @Size(max = 50, message = "Thành phố không được vượt quá 50 ký tự")
    private String town;

    @Size(max = 50, message = "Tỉnh không được vượt quá 50 ký tự")
    private String state;

    private Integer zipCode;

    @Pattern(regexp = "^[0-9+\\-\\s()]+$", message = "Số điện thoại không hợp lệ")
    @Size(max = 20, message = "Số điện thoại không được vượt quá 20 ký tự")
    private String phone;

    @Email(message = "Email không hợp lệ")
    @Size(max = 100, message = "Email không được vượt quá 100 ký tự")
    private String email;
}
