package com.uniclub.dto.request.Variant;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateVariantRequest {

    @NotNull(message = "SKU của variant không được để trống")
    private Integer sku;

    @NotNull(message = "ID sản phẩm không được để trống")
    private Integer idProduct;

    @NotNull(message = "ID sản phẩm không được để trống")
    private Integer idSize;

    @NotNull(message = "ID sản phẩm không được để trống")
    private Integer idColor;

    @Size(max = 255, message = "Đường dẫn hình ảnh không được vượt quá 255 ký tự")
    private String images;

    @PositiveOrZero(message = "Số lượng phải lớn hơn hoặc bằng 0")
    private Integer quantity;

    @PositiveOrZero(message = "Giá phải lớn hơn hoặc bằng 0")
    private Integer price;

    private Byte status; // 1 = hoạt động, 0 = ngừng kinh doanh
}
