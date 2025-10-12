package com.uniclub.dto.response.Payment;

import lombok.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.uniclub.entity.Payment;
import com.uniclub.entity.enums.PaymentMethod;
import com.uniclub.entity.enums.PaymentStatus;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentResponse {

    private Integer id;
    private PaymentMethod paymentMethod;
    private String transactionNo;
    private Integer amount;
    private PaymentStatus paymentStatus;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime paidAt;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
    
    private Integer orderId;

    //Hàm chuyển từ Entity sang DTO Response
    public static PaymentResponse fromEntity(Payment payment) {
        if (payment == null) return null;
        return PaymentResponse.builder()
                .id(payment.getId())
                .paymentMethod(payment.getPaymentMethod())
                .transactionNo(payment.getTransactionNo())
                .amount(payment.getAmount())
                .paymentStatus(payment.getPaymentStatus())
                .paidAt(payment.getPaidAt())
                .createdAt(payment.getCreatedAt())
                .updatedAt(payment.getUpdatedAt())
                .orderId(payment.getOrder() != null ? payment.getOrder().getId() : null)
                .build();
    }
}
