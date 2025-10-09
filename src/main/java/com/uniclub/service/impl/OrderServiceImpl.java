package com.uniclub.service.impl;

import com.uniclub.dto.request.Order.CreateOrderRequest;
import com.uniclub.dto.response.Order.OrderResponse;
import com.uniclub.entity.Order;
import com.uniclub.entity.OrderVariant;
import com.uniclub.entity.PaymentMethod;
import com.uniclub.entity.User;
import com.uniclub.entity.Variant;
import com.uniclub.exception.ResourceNotFoundException;
import com.uniclub.repository.OrderRepository;
import com.uniclub.repository.PaymentMethodRepository;
import com.uniclub.repository.UserRepository;
import com.uniclub.repository.VariantRepository;
import com.uniclub.service.OrderService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    @Autowired
    private VariantRepository variantRepository;

    @Override
    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(OrderResponse::fromEntity)
                .toList();
    }

    @Override
    public OrderResponse getOrderById(Integer id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", id));
        return OrderResponse.fromEntity(order);
    }

    @Override
    public OrderResponse createOrder(CreateOrderRequest request) {
        Order order = buildOrderFromRequest(request);
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());
        Order savedOrder = orderRepository.save(order);
        return OrderResponse.fromEntity(savedOrder);
    }

    @Override
    public OrderResponse updateOrder(Integer id, CreateOrderRequest request) {
        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", id));

        // Update basic info
        existingOrder.setNote(request.getNote());
        existingOrder.setUpdatedAt(LocalDateTime.now());

        // Update User if changed
        if (!existingOrder.getUser().getId().equals(request.getUserId())) {
            User user = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User", "id", request.getUserId()));
            existingOrder.setUser(user);
        }

        // Update PaymentMethod if changed
        if (!existingOrder.getPaymentMethod().getId().equals(request.getPaymentMethodId())) {
            PaymentMethod pm = paymentMethodRepository.findById(request.getPaymentMethodId())
                    .orElseThrow(() -> new ResourceNotFoundException("PaymentMethod", "id", request.getPaymentMethodId()));
            existingOrder.setPaymentMethod(pm);
        }

        // Update OrderVariants (replace all)
        existingOrder.getOrderVariants().clear();
        List<OrderVariant> updatedVariants = request.getOrderItems().stream().map(item -> {
            Variant variant = variantRepository.findById(item.getSkuVariant())
                    .orElseThrow(() -> new ResourceNotFoundException("Variant", "sku", item.getSkuVariant()));

            OrderVariant ov = new OrderVariant();
            ov.setOrder(existingOrder);
            ov.setVariant(variant);
            ov.setQuantity(item.getQuantity());
            ov.setPrice(item.getPrice());
            return ov;
        }).toList();

        existingOrder.setOrderVariants(updatedVariants);

        // Update total
        int total = updatedVariants.stream().mapToInt(ov -> ov.getPrice() * ov.getQuantity()).sum();
        existingOrder.setTotal(total);

        Order savedOrder = orderRepository.save(existingOrder);
        return OrderResponse.fromEntity(savedOrder);
    }

    @Override
    public void deleteOrder(Integer id) {
        if (!orderRepository.existsById(id)) {
            throw new ResourceNotFoundException("Order", "id", id);
        }
        orderRepository.deleteById(id);
    }

    // =====================
    // Helper method
    // =====================
    private Order buildOrderFromRequest(CreateOrderRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", request.getUserId()));

        PaymentMethod pm = paymentMethodRepository.findById(request.getPaymentMethodId())
                .orElseThrow(() -> new ResourceNotFoundException("PaymentMethod", "id", request.getPaymentMethodId()));

        Order order = new Order();
        order.setUser(user);
        order.setPaymentMethod(pm);
        order.setNote(request.getNote());
        order.setStatus("PENDING");

        List<OrderVariant> variants = request.getOrderItems().stream().map(item -> {
            Variant variant = variantRepository.findById(item.getSkuVariant())
                    .orElseThrow(() -> new ResourceNotFoundException("Variant", "sku", item.getSkuVariant()));

            OrderVariant ov = new OrderVariant();
            ov.setOrder(order);
            ov.setVariant(variant);
            ov.setQuantity(item.getQuantity());
            ov.setPrice(item.getPrice());
            return ov;
        }).toList();

        order.setOrderVariants(variants);
        int total = variants.stream().mapToInt(ov -> ov.getPrice() * ov.getQuantity()).sum();
        order.setTotal(total);

        return order;
    }
}
