package com.uniclub.dto.request.User;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateUserRequest {
    // Email is optional - it should not be changed as it's used for login
    @Email(message = "Email không hợp lệ")
    private String email;

    @Size(min = 6, message = "Mật khẩu phải có ít nhất 6 ký tự")
    private String password;

    // Fullname is optional in update - will use existing value if not provided
    private String fullname;

    // Default address fields
    private String phone;
    private String address;
    private String provinceCode;
    private String provinceName;
    private String districtCode;
    private String districtName;
    private String wardCode;
    private String wardName;

    private Integer roleId;
    private Byte status;
}