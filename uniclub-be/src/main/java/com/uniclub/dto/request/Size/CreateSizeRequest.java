package com.uniclub.dto.request.Size;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateSizeRequest {
    @NotBlank(message = "Tên Size được để trống")
    @Size(max = 20, message = "Tên Size được vượt quá 20 ký tự")
    private String name;

}
