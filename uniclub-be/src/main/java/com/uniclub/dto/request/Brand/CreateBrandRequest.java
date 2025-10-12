package com.uniclub.dto.request.Brand;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


@Data
public class CreateBrandRequest {
    @NotBlank(message = "Tên thương hiệu không được để trống")
    @Size(max = 50, message = "Tên thương hiệu không được vượt quá 50 ký tự")
    private String name;
}
