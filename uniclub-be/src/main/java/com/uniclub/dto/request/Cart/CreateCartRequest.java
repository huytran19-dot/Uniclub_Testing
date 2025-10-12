package com.uniclub.dto.request.Cart;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Data
public class CreateCartRequest {
    @NotNull(message = "ID người dùng không được để trống")
    private Integer userId;

    @Size(max = 255, message = "Ghi chú không được vượt quá 255 ký tự")
    private String note;
}
