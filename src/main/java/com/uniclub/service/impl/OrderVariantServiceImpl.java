package com.uniclub.service.impl;

import com.uniclub.dto.request.Order.CreateOrderVariantRequest;
import com.uniclub.dto.response.Order.OrderVariantResponse;
import com.uniclub.entity.Order;
import com.uniclub.entity.OrderVariant;
import com.uniclub.entity.OrderVariantId;
import com.uniclub.entity.Variant;
import com.uniclub.exception.ResourceNotFoundException;
import com.uniclub.repository.OrderRepository;
import com.uniclub.repository.OrderVariantRepository;
import com.uniclub.repository.VariantRepository;
import com.uniclub.service.OrderVariantService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class OrderVariantServiceImpl implements OrderVariantService {
    @Autowired
    private OrderVariantRepository orderVariantRepository;
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private VariantRepository variantRepository;

    @Override
    public OrderVariantResponse createOrderVariant(Integer orderId, CreateOrderVariantRequest request) {
        // Check if order exists
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", orderId));

        // Check if variant exists
        Variant variant = variantRepository.findById(request.getSkuVariant())
                .orElseThrow(() -> new ResourceNotFoundException("Variant", "sku", request.getSkuVariant()));

        // Check if order variant already exists
        OrderVariantId id = new OrderVariantId();
        id.setOrder(order.getId());
        id.setVariant(variant.getSku());
        
        if (orderVariantRepository.existsById(id)) {
            throw new IllegalArgumentException("Sản phẩm này đã có trong đơn hàng");
        }

        // Check if variant has enough quantity
        if (variant.getQuantity() < request.getQuantity()) {
            throw new IllegalArgumentException("Số lượng sản phẩm không đủ");
        }

        OrderVariant orderVariant = new OrderVariant();
        orderVariant.setOrder(order);
        orderVariant.setVariant(variant);
        orderVariant.setQuantity(request.getQuantity());
        orderVariant.setPrice(request.getPrice());

        OrderVariant savedOrderVariant = orderVariantRepository.save(orderVariant);
        return OrderVariantResponse.fromEntity(savedOrderVariant);
    }

    @Override
    public OrderVariantResponse updateOrderVariant(Integer orderId, Integer variantSku, Integer quantity, Integer price) {
        // Find the order variant
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", orderId));
        Variant variant = variantRepository.findById(variantSku)
                .orElseThrow(() -> new ResourceNotFoundException("Variant", "sku", variantSku));

        OrderVariantId id = new OrderVariantId();
        id.setOrder(order.getId());
        id.setVariant(variant.getSku());
        
        OrderVariant orderVariant = orderVariantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("OrderVariant", "id", id));

        if (quantity != null) {
            // Check if variant has enough quantity
            if (variant.getQuantity() < quantity) {
                throw new IllegalArgumentException("Số lượng sản phẩm không đủ");
            }
            orderVariant.setQuantity(quantity);
        }
        if (price != null) {
            orderVariant.setPrice(price);
        }

        OrderVariant updatedOrderVariant = orderVariantRepository.save(orderVariant);
        return OrderVariantResponse.fromEntity(updatedOrderVariant);
    }

    @Override
    public List<OrderVariantResponse> getAllOrderVariants() {
        return orderVariantRepository.findAll()
                .stream()
                .map(OrderVariantResponse::fromEntity)
                .toList();
    }

    @Override
    public OrderVariantResponse getOrderVariantById(Integer orderId, Integer variantSku) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", orderId));
        Variant variant = variantRepository.findById(variantSku)
                .orElseThrow(() -> new ResourceNotFoundException("Variant", "sku", variantSku));

        OrderVariantId id = new OrderVariantId();
        id.setOrder(order.getId());
        id.setVariant(variant.getSku());
        
        OrderVariant orderVariant = orderVariantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("OrderVariant", "id", id));
        return OrderVariantResponse.fromEntity(orderVariant);
    }

    @Override
    public List<OrderVariantResponse> getOrderVariantsByOrderId(Integer orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", orderId));
        
        return orderVariantRepository.findByOrder(order)
                .stream()
                .map(OrderVariantResponse::fromEntity)
                .toList();
    }

    @Override
    public void deleteOrderVariant(Integer orderId, Integer variantSku) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", orderId));
        Variant variant = variantRepository.findById(variantSku)
                .orElseThrow(() -> new ResourceNotFoundException("Variant", "sku", variantSku));

        OrderVariantId id = new OrderVariantId();
        id.setOrder(order.getId());
        id.setVariant(variant.getSku());
        
        if (!orderVariantRepository.existsById(id)) {
            throw new ResourceNotFoundException("OrderVariant", "id", id);
        }
        orderVariantRepository.deleteById(id);
    }
}
