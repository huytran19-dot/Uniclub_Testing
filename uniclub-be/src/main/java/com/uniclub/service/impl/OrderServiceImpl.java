package com.uniclub.service.impl;

import com.uniclub.dto.request.Order.CreateOrderRequest;
import com.uniclub.dto.response.Order.OrderResponse;
import com.uniclub.entity.Order;
import com.uniclub.entity.OrderVariant;
import com.uniclub.entity.User;
import com.uniclub.entity.Variant;
import com.uniclub.exception.ResourceNotFoundException;
import com.uniclub.repository.OrderRepository;
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
        // Validate order variants
        if (request.getOrderVariants() == null || request.getOrderVariants().isEmpty()) {
            throw new IllegalArgumentException("Đơn hàng phải có ít nhất một sản phẩm");
        }

        // Check if all variants exist and have enough stock
        for (var item : request.getOrderVariants()) {
            Variant variant = variantRepository.findById(item.getSkuVariant())
                    .orElseThrow(() -> new ResourceNotFoundException("Variant", "sku", item.getSkuVariant()));
            
            if (variant.getQuantity() < item.getQuantity()) {
                throw new IllegalArgumentException("Sản phẩm " + variant.getSku() + " không đủ số lượng");
            }
        }

        Order order = buildOrderFromRequest(request);
        Order savedOrder = orderRepository.save(order);
        return OrderResponse.fromEntity(savedOrder);
    }

    @Override
    public OrderResponse updateOrder(Integer id, CreateOrderRequest request) {
        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", id));

        // Validate order variants if provided
        if (request.getOrderVariants() != null && !request.getOrderVariants().isEmpty()) {
            // Check if all variants exist and have enough stock
            for (var item : request.getOrderVariants()) {
                Variant variant = variantRepository.findById(item.getSkuVariant())
                        .orElseThrow(() -> new ResourceNotFoundException("Variant", "sku", item.getSkuVariant()));
                
                if (variant.getQuantity() < item.getQuantity()) {
                    throw new IllegalArgumentException("Sản phẩm " + variant.getSku() + " không đủ số lượng");
                }
            }
        }

        // Update basic info
        if (request.getNote() != null) {
            existingOrder.setNote(request.getNote());
        }
        existingOrder.setUpdatedAt(LocalDateTime.now());

        // Update User if provided and changed
        if (request.getUserId() != null && !existingOrder.getUser().getId().equals(request.getUserId())) {
            User user = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User", "id", request.getUserId()));
            existingOrder.setUser(user);
        }


        // Update OrderVariants if provided
        if (request.getOrderVariants() != null && !request.getOrderVariants().isEmpty()) {
            existingOrder.getOrderVariants().clear();
            List<OrderVariant> updatedVariants = request.getOrderVariants().stream().map(item -> {
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
        }

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

        Order order = new Order();
        order.setUser(user);
        order.setNote(request.getNote());
        order.setStatus("PENDING");

        // Calculate total first
        int total = request.getOrderVariants().stream()
                .mapToInt(item -> item.getPrice() * item.getQuantity())
                .sum();
        order.setTotal(total);

        // Create OrderVariants after order is saved
        List<OrderVariant> variants = request.getOrderVariants().stream().map(item -> {
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

        return order;
    }
}
