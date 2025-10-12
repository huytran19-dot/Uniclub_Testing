package com.uniclub.service;

import com.uniclub.dto.request.Order.CreateOrderVariantRequest;
import com.uniclub.dto.response.Order.OrderVariantResponse;

import java.util.List;

public interface OrderVariantService {
    OrderVariantResponse createOrderVariant(Integer orderId, CreateOrderVariantRequest request);
    OrderVariantResponse updateOrderVariant(Integer orderId, Integer variantSku, Integer quantity, Integer price);
    void deleteOrderVariant(Integer orderId, Integer variantSku);

    List<OrderVariantResponse> getAllOrderVariants();
    OrderVariantResponse getOrderVariantById(Integer orderId, Integer variantSku);
    List<OrderVariantResponse> getOrderVariantsByOrderId(Integer orderId);
}
