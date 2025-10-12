package com.uniclub.dto.request.Category;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class UpdateCategoryRequest {
    @Size(max = 50, message = "Tên danh mục không được vượt quá 50 ký tự")
    private String name;

    private Byte status;
}
