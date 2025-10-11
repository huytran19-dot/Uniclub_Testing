package com.uniclub.dto.request.Size;

import lombok.Data;
import jakarta.validation.constraints.Size;

@Data
public class UpdateSizeRequest {
    @Size(max = 20, message = "Tên kích thước không được vượt quá 20 ký tự")
    private String name;

    private Integer status;
}
