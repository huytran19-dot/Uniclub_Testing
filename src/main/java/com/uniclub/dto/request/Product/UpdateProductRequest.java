package com.uniclub.dto.request.Product;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UpdateProductRequest {

    @NotNull(message = "ID sản phẩm không được để trống")
    private Integer id;

    @NotBlank(message = "Tên sản phẩm không được để trống")
    @Size(max = 255, message = "Tên sản phẩm không được vượt quá 255 ký tự")
    private String name;

    @Size(max = 255, message = "Mô tả không được vượt quá 255 ký tự")
    private String description;

    @Size(max = 255, message = "Thông tin không được vượt quá 255 ký tự")
    private String information;

    @PositiveOrZero(message = "Giá sản phẩm phải lớn hơn hoặc bằng 0")
    private double price;

    private Integer idBrand;
    private Integer idCategory;
    private Byte status;
}
