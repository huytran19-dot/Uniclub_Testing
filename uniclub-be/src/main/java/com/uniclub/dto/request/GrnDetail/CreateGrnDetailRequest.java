package com.uniclub.dto.request.GrnDetail;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Data
public class CreateGrnDetailRequest {
    @NotNull(message = "ID phiếu nhập không được để trống")
    private Integer grnHeaderId;

    @NotNull(message = "SKU sản phẩm không được để trống")
    private Integer variantSku;

    @NotNull(message = "Số lượng không được để trống")
    @Positive(message = "Số lượng phải lớn hơn 0")
    private Integer quantity;

    @NotNull(message = "Giá đơn vị không được để trống")
    @Positive(message = "Giá đơn vị phải lớn hơn 0")
    private Integer unitCost;
}
