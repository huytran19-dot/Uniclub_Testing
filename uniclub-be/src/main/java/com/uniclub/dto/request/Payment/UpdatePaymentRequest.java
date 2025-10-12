package com.uniclub.dto.request.Payment;

import lombok.Data;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import com.uniclub.entity.enums.PaymentStatus;

@Data
public class UpdatePaymentRequest {
    @Size(max = 100, message = "Mã giao dịch không được vượt quá 100 ký tự")
    private String transactionNo;

    @Positive(message = "Số tiền phải lớn hơn 0")
    private Integer amount;

    private PaymentStatus paymentStatus;
}
