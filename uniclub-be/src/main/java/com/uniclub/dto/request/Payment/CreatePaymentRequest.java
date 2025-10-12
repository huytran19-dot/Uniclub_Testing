package com.uniclub.dto.request.Payment;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import com.uniclub.entity.enums.PaymentMethod;

@Data
public class CreatePaymentRequest {
    @NotNull(message = "Phương thức thanh toán không được để trống")
    private PaymentMethod paymentMethod;

    @Size(max = 100, message = "Mã giao dịch không được vượt quá 100 ký tự")
    private String transactionNo;

    @NotNull(message = "Số tiền không được để trống")
    @Positive(message = "Số tiền phải lớn hơn 0")
    private Integer amount;

    @NotNull(message = "ID đơn hàng không được để trống")
    private Integer orderId;
}
