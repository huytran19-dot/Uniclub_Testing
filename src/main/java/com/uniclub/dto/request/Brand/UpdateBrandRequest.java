package com.uniclub.dto.request.Brand;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateBrandRequest {

    @NotNull(message = "ID thương hiệu không được để trống")
    private Integer id;

    @NotBlank(message = "Tên thương hiệu không được để trống")
    @Size(max = 50, message = "Tên thương hiệu không được vượt quá 50 ký tự")
    private String name;

    private Byte status;


}
