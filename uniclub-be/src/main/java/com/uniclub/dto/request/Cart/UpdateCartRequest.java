package com.uniclub.dto.request.Cart;

import lombok.Data;
import jakarta.validation.constraints.Size;

@Data
public class UpdateCartRequest {
    @Size(max = 255, message = "Ghi chú không được vượt quá 255 ký tự")
    private String note;

    private Byte status;
}
