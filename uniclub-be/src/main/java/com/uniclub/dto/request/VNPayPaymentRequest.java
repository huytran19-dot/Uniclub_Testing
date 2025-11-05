package com.uniclub.dto.request;

import lombok.Data;

@Data
public class VNPayPaymentRequest {
    private Integer orderId;
    private String orderInfo;
    private String locale = "vn"; // vn or en
}
