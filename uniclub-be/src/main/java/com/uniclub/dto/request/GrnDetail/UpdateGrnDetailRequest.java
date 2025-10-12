package com.uniclub.dto.request.GrnDetail;

import lombok.Data;
import jakarta.validation.constraints.Positive;

@Data
public class UpdateGrnDetailRequest {
    @Positive(message = "Số lượng phải lớn hơn 0")
    private Integer quantity;

    @Positive(message = "Giá đơn vị phải lớn hơn 0")
    private Integer unitCost;
}
