package com.uniclub.repository;

import com.uniclub.dto.request.Order.CreateOrderRequest;
import com.uniclub.dto.response.Order.OrderResponse;
import com.uniclub.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<OrderResponse> getAllOrders();
    OrderResponse getOrderById(Integer id);
    OrderResponse createOrder(CreateOrderRequest request);
    OrderResponse updateOrder(Integer id, CreateOrderRequest request); // dùng cùng DTO create/update
    void deleteOrder(Integer id);
}
