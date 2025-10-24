package com.uniclub.dto.request.Product;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CreateProductRequest {

    @NotBlank(message = "Tên sản phẩm không được để trống")
    @Size(max = 255, message = "Tên sản phẩm không được vượt quá 255 ký tự")
    private String name;

    @Size(max = 255, message = "Mô tả không được vượt quá 255 ký tự")
    private String description;

    @Size(max = 255, message = "Thông tin không được vượt quá 255 ký tự")
    private String information;

    @NotNull(message = "ID thương hiệu không được để trống")
    private Integer brandId;

    @NotNull(message = "ID danh mục không được để trống")
    private Integer categoryId;
}
