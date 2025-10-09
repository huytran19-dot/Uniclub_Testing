package com.uniclub.dto.request.Color;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateColorRequest {
    @NotBlank(message = "Tên màu không được để trống")
    @Size(max = 50, message = "Tên màu không được vượt quá 50 ký tự")
    private String name;
}
