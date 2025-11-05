package com.uniclub.service;

import java.util.List;

import com.uniclub.dto.request.Order.CreateOrderRequest;
import com.uniclub.dto.response.Order.OrderResponse;

public interface OrderService {
    List<OrderResponse> getAllOrders();
    List<OrderResponse> getOrdersByUserId(Integer userId);
    OrderResponse getOrderById(Integer id);
    OrderResponse createOrder(CreateOrderRequest request);
    OrderResponse updateOrder(Integer id, CreateOrderRequest request);
    OrderResponse updateOrderStatus(Integer id, CreateOrderRequest request);
    OrderResponse cancelOrder(Integer id);
    void deleteOrder(Integer id);
}
