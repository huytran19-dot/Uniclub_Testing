package com.uniclub.dto.request.Variant;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateVariantRequest {
    private Integer productId;
    private Integer sizeId;
    private Integer colorId;

    @Size(max = 255, message = "Đường dẫn hình ảnh không được vượt quá 255 ký tự")
    private String images;

    @PositiveOrZero(message = "Số lượng phải lớn hơn hoặc bằng 0")
    private Integer quantity;

    @PositiveOrZero(message = "Giá phải lớn hơn hoặc bằng 0")
    private Integer price;

    private Byte status;
}
