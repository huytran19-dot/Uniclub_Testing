package com.uniclub.dto.response.Order;

import com.uniclub.dto.response.PaymentMethod.PaymentMethodResponse;
import com.uniclub.dto.response.User.UserResponse;
import com.uniclub.entity.Order;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderResponse {
    private Integer id;
    private Integer total;
    private String note;
    private PaymentMethodResponse paymentMethod;
    private UserResponse user;
    private List<OrderVariantResponse> orderVariants;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static OrderResponse fromEntity(Order order) {
        OrderResponse res = new OrderResponse();

        res.setId(order.getId());
        res.setTotal(order.getTotal());
        res.setNote(order.getNote());
        res.setStatus(order.getStatus());

        if (order.getUser() != null) {
            res.setUser(UserResponse.fromEntity(order.getUser()));
        }

        if (order.getPaymentMethod() != null) {
            res.setPaymentMethod(PaymentMethodResponse.fromEntity(order.getPaymentMethod()));
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