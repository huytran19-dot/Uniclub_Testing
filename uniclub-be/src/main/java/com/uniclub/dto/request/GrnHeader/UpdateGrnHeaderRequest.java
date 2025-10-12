package com.uniclub.dto.request.GrnHeader;

import lombok.Data;
import jakarta.validation.constraints.Size;
import com.uniclub.entity.enums.GrnStatus;

import java.time.LocalDate;

@Data
public class UpdateGrnHeaderRequest {
    @Size(max = 255, message = "Ghi chú không được vượt quá 255 ký tự")
    private String note;

    private LocalDate receivedDate;
    private GrnStatus status;
}
