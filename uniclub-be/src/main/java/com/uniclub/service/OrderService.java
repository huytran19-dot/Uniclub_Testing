package com.uniclub.service;

import com.uniclub.dto.request.Order.CreateOrderRequest;
import com.uniclub.dto.response.Order.OrderResponse;

import java.util.List;

public interface OrderService {
    List<OrderResponse> getAllOrders();
    OrderResponse getOrderById(Integer id);
    OrderResponse createOrder(CreateOrderRequest request);
    OrderResponse updateOrder(Integer id, CreateOrderRequest request);
    OrderResponse updateOrderStatus(Integer id, CreateOrderRequest request);
    void deleteOrder(Integer id);
}
