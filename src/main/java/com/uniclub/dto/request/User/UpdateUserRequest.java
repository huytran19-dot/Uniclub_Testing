package com.uniclub.dto.request.User;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateUserRequest {
    @Email(message = "Email không hợp lệ")
    @Size(max = 50, message = "Email không được vượt quá 50 ký tự")
    private String email;

    @Size(min = 6, max = 255, message = "Mật khẩu phải có từ 6 đến 255 ký tự")
    private String password;

    @Size(max = 255, message = "Tên không được vượt quá 255 ký tự")
    private String fullname;

    private Integer roleId;

    private Byte status;
}
