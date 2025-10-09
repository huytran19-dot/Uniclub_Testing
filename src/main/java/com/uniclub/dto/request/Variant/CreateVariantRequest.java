package com.uniclub.dto.request.Variant;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateVariantRequest {

    @NotNull(message = "Id sản phẩm không được để trống")
    private Integer idProduct;

    private Integer idSize;
    private Integer idColor;

    @Size(max = 255, message = "Độ dài tối đa của images là 255 ký tự")
    private String images;

    @Min(value = 0, message = "Số lượng phải >= 0")
    private Integer quantity;

    @Min(value = 0, message = "Giá phải >= 0")
    private Integer price;

    private Byte status = 1;
}
