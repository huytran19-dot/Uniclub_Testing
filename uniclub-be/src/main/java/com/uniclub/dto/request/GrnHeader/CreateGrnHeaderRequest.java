package com.uniclub.dto.request.GrnHeader;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

@Data
public class CreateGrnHeaderRequest {
    @NotNull(message = "ID nhà cung cấp không được để trống")
    private Integer supplierId;

    @Size(max = 255, message = "Ghi chú không được vượt quá 255 ký tự")
    private String note;

    private LocalDate receivedDate;
}
