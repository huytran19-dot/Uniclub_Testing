package com.uniclub.dto.response.Order;

import java.time.LocalDateTime;
import java.util.List;

import com.uniclub.dto.response.User.UserResponse;
import com.uniclub.entity.Order;

import lombok.Data;

@Data
public class OrderResponse {
    private Integer id;
    private Integer total;
    private Integer shippingFee;
    private String note;
    private String recipientName;
    private String recipientPhone;
    private String shippingAddress;
    private String paymentMethod; // COD or VNPay
    private UserResponse user;
    private List<OrderVariantResponse> orderVariants;
    private String status;
    private LocalDateTime paymentExpiresAt; // Payment timeout for VNPay
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static OrderResponse fromEntity(Order order) {
        OrderResponse res = new OrderResponse();

        res.setId(order.getId());
        res.setTotal(order.getTotal());
        res.setShippingFee(order.getShippingFee());
        res.setNote(order.getNote());
        res.setRecipientName(order.getRecipientName());
        res.setRecipientPhone(order.getRecipientPhone());
        res.setShippingAddress(order.getShippingAddress());
        res.setStatus(order.getStatus() != null ? order.getStatus().name() : null);
        res.setPaymentExpiresAt(order.getPaymentExpiresAt());
        
        // Note: paymentMethod will be set separately by service layer
        res.setPaymentMethod("COD"); // Default value

        if (order.getUser() != null) {
            res.setUser(UserResponse.fromEntity(order.getUser()));
        }

        if (order.getOrderVariants() != null) {
            res.setOrderVariants(
                    order.getOrderVariants().stream()
                            .map(OrderVariantResponse::fromEntity)
                            .toList()
            );
        }

        res.setCreatedAt(order.getCreatedAt());
        res.setUpdatedAt(order.getUpdatedAt());

        return res;
    }
}