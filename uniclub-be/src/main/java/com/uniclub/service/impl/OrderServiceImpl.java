package com.uniclub.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uniclub.dto.request.Order.CreateOrderRequest;
import com.uniclub.dto.response.Order.OrderResponse;
import com.uniclub.entity.Order;
import com.uniclub.entity.OrderVariant;
import com.uniclub.entity.Payment;
import com.uniclub.entity.User;
import com.uniclub.entity.Variant;
import com.uniclub.entity.enums.OrderStatus;
import com.uniclub.entity.enums.PaymentStatus;
import com.uniclub.exception.ResourceNotFoundException;
import com.uniclub.repository.OrderRepository;
import com.uniclub.repository.PaymentRepository;
import com.uniclub.repository.UserRepository;
import com.uniclub.repository.VariantRepository;
import com.uniclub.service.CartService;
import com.uniclub.service.OrderService;
import com.uniclub.service.VNPayService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VariantRepository variantRepository;
    
    @Autowired
    private PaymentRepository paymentRepository;
    
    @Autowired
    private CartService cartService;
    
    @Autowired
    private VNPayService vnPayService;

    @Override
    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(order -> {
                    OrderResponse response = OrderResponse.fromEntity(order);
                    enrichOrderResponseWithPaymentMethod(response, order);
                    return response;
                })
                .toList();
    }
    
    @Override
    public List<OrderResponse> getOrdersByUserId(Integer userId) {
        return orderRepository.findByUserId(userId).stream()
                .map(order -> {
                    OrderResponse response = OrderResponse.fromEntity(order);
                    enrichOrderResponseWithPaymentMethod(response, order);
                    return response;
                })
                .toList();
    }

    @Override
    public OrderResponse getOrderById(Integer id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", id));
        OrderResponse response = OrderResponse.fromEntity(order);
        enrichOrderResponseWithPaymentMethod(response, order);
        return response;
    }

    @Override
    public OrderResponse createOrder(CreateOrderRequest request) {
        // Validate order variants
        if (request.getOrderVariants() == null || request.getOrderVariants().isEmpty()) {
            throw new IllegalArgumentException("Đơn hàng phải có ít nhất một sản phẩm");
        }

        // Check if all variants exist and have enough stock
        for (var item : request.getOrderVariants()) {
            Variant variant = variantRepository.findById(item.getVariantSku())
                    .orElseThrow(() -> new ResourceNotFoundException("Variant", "sku", item.getVariantSku()));
            
            if (variant.getQuantity() < item.getQuantity()) {
                throw new IllegalArgumentException("Sản phẩm " + variant.getSku() + " không đủ số lượng");
            }
        }

        // Build and save order
        Order order = buildOrderFromRequest(request);
        Order savedOrder = orderRepository.save(order);
        
        // Create payment record
        Payment payment = new Payment();
        payment.setOrder(savedOrder);
        payment.setPaymentMethod(request.getPaymentMethod());
        payment.setAmount(savedOrder.getTotal());
        payment.setPaymentStatus(PaymentStatus.PENDING);
        paymentRepository.save(payment);
        
        // Reduce variant quantities
        for (var item : request.getOrderVariants()) {
            Variant variant = variantRepository.findById(item.getVariantSku())
                    .orElseThrow(() -> new ResourceNotFoundException("Variant", "sku", item.getVariantSku()));
            variant.setQuantity(variant.getQuantity() - item.getQuantity());
            variantRepository.save(variant);
        }
        
        // Clear user's cart after successful order
        cartService.clearCartByUserId(request.getUserId());
        
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
                Variant variant = variantRepository.findById(item.getVariantSku())
                        .orElseThrow(() -> new ResourceNotFoundException("Variant", "sku", item.getVariantSku()));
                
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
                Variant variant = variantRepository.findById(item.getVariantSku())
                        .orElseThrow(() -> new ResourceNotFoundException("Variant", "sku", item.getVariantSku()));

                OrderVariant ov = new OrderVariant();
                ov.setOrder(existingOrder);
                ov.setVariant(variant);
                ov.setQuantity(item.getQuantity());
                ov.setPrice(item.getPrice());
                return ov;
            }).toList();

            existingOrder.setOrderVariants(updatedVariants);

            // Update total with shipping fee
            int subtotal = updatedVariants.stream().mapToInt(ov -> ov.getPrice() * ov.getQuantity()).sum();
            int shippingFee = subtotal >= 499000 ? 0 : 30000;
            existingOrder.setShippingFee(shippingFee);
            int total = subtotal + shippingFee;
            existingOrder.setTotal(total);
        }

        Order savedOrder = orderRepository.save(existingOrder);
        return OrderResponse.fromEntity(savedOrder);
    }

    @Override
    public OrderResponse updateOrderStatus(Integer id, CreateOrderRequest request) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", id));

        // Check if order is already completed or cancelled
        if (OrderStatus.DELIVERED.equals(order.getStatus()) || OrderStatus.CANCELLED.equals(order.getStatus())) {
            throw new IllegalArgumentException("Không thể cập nhật trạng thái đơn hàng đã hoàn thành hoặc bị hủy");
        }

        // Update status if provided
        if (request.getStatus() != null) {
            order.setStatus(request.getStatus());
            
            // Auto-update payment to SUCCESS when order is DELIVERED
            if (OrderStatus.DELIVERED.equals(request.getStatus())) {
                List<Payment> payments = paymentRepository.findByOrderId(order.getId());
                for (Payment payment : payments) {
                    if (PaymentStatus.PENDING.equals(payment.getPaymentStatus())) {
                        payment.setPaymentStatus(PaymentStatus.SUCCESS);
                        payment.setPaidAt(LocalDateTime.now());
                        paymentRepository.save(payment);
                    }
                }
            }
        }

        Order savedOrder = orderRepository.save(order);
        return OrderResponse.fromEntity(savedOrder);
    }

    @Override
    public OrderResponse cancelOrder(Integer id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", id));

        // Check if order can be cancelled
        // Customer can cancel if order is PENDING or CONFIRMED, but NOT if SHIPPING, DELIVERED, or already CANCELLED
        if (OrderStatus.SHIPPING.equals(order.getStatus())) {
            throw new IllegalArgumentException("Không thể hủy đơn hàng đang được giao");
        }
        if (OrderStatus.DELIVERED.equals(order.getStatus())) {
            throw new IllegalArgumentException("Không thể hủy đơn hàng đã hoàn thành");
        }
        if (OrderStatus.CANCELLED.equals(order.getStatus())) {
            throw new IllegalArgumentException("Đơn hàng đã được hủy trước đó");
        }

        // Update order status to CANCELLED
        order.setStatus(OrderStatus.CANCELLED);
        
        // Update payment status to CANCELLED
        List<Payment> payments = paymentRepository.findByOrderId(order.getId());
        for (Payment payment : payments) {
            if (!PaymentStatus.SUCCESS.equals(payment.getPaymentStatus())) {
                payment.setPaymentStatus(PaymentStatus.CANCELLED);
                paymentRepository.save(payment);
            }
        }
        
        // Restore variant quantities
        for (OrderVariant orderVariant : order.getOrderVariants()) {
            Variant variant = orderVariant.getVariant();
            variant.setQuantity(variant.getQuantity() + orderVariant.getQuantity());
            variantRepository.save(variant);
        }

        Order savedOrder = orderRepository.save(order);
        OrderResponse response = OrderResponse.fromEntity(savedOrder);
        enrichOrderResponseWithPaymentMethod(response, savedOrder);
        return response;
    }

    @Override
    public void deleteOrder(Integer id) {
        if (!orderRepository.existsById(id)) {
            throw new ResourceNotFoundException("Order", "id", id);
        }
        orderRepository.deleteById(id);
    }

    // =====================
    // Helper methods
    // =====================
    
    /**
     * Enriches OrderResponse with payment method from Payment entity
     * @param response The OrderResponse to enrich
     * @param order The Order entity to query payment for
     */
    private void enrichOrderResponseWithPaymentMethod(OrderResponse response, Order order) {
        List<Payment> payments = paymentRepository.findByOrderId(order.getId());
        if (!payments.isEmpty()) {
            Payment payment = payments.get(0); // Get first payment record
            response.setPaymentMethod(payment.getPaymentMethod().name());
        }
        // Otherwise keep default "COD" value set by OrderResponse.fromEntity()
    }
    
    private Order buildOrderFromRequest(CreateOrderRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", request.getUserId()));

        Order order = new Order();
        order.setUser(user);
        order.setNote(request.getNote());
        order.setStatus(OrderStatus.PENDING);
        order.setRecipientName(request.getRecipientName());
        order.setRecipientPhone(request.getRecipientPhone());
        order.setShippingAddress(request.getShippingAddress());

        // Calculate subtotal from order variants
        int subtotal = request.getOrderVariants().stream()
                .mapToInt(item -> item.getPrice() * item.getQuantity())
                .sum();
        
        // Calculate shipping fee: free if subtotal >= 499,000, otherwise 30,000
        int shippingFee = subtotal >= 499000 ? 0 : 30000;
        order.setShippingFee(shippingFee);
        
        // Calculate total = subtotal + shipping fee
        int total = subtotal + shippingFee;
        order.setTotal(total);

        // Create OrderVariants after order is saved
        List<OrderVariant> variants = request.getOrderVariants().stream().map(item -> {
            Variant variant = variantRepository.findById(item.getVariantSku())
                    .orElseThrow(() -> new ResourceNotFoundException("Variant", "sku", item.getVariantSku()));

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
    
    @Override
    public String retryVNPayPayment(Integer orderId) {
        // Get order
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", orderId));
        
        // Check if order can retry payment
        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new IllegalArgumentException("Không thể thanh toán lại đơn hàng đã bị hủy");
        }
        
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new IllegalArgumentException("Chỉ có thể thanh toán lại đơn hàng ở trạng thái PENDING");
        }
        
        // Get latest payment to verify it's VNPay
        var latestPayment = paymentRepository.findLatestByOrderId(orderId);
        if (latestPayment.isEmpty()) {
            throw new IllegalArgumentException("Không tìm thấy thông tin thanh toán");
        }
        
        // Create new payment URL (VNPayService will create new payment record with new expiration)
        String paymentUrl = vnPayService.createPaymentUrl(
            orderId,
            "Thanh toan lai don hang " + orderId,
            "127.0.0.1",
            "vn"
        );
        
        return paymentUrl;
    }
}

