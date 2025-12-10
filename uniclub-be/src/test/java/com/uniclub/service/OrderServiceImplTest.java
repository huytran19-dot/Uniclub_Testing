package com.uniclub.service;

import com.uniclub.dto.request.Order.CreateOrderRequest;
import com.uniclub.dto.request.Order.CreateOrderVariantRequest;
import com.uniclub.dto.response.Order.OrderResponse;
import com.uniclub.entity.Order;
import com.uniclub.entity.OrderVariant;
import com.uniclub.entity.Payment;
import com.uniclub.entity.User;
import com.uniclub.entity.Variant;
import com.uniclub.entity.enums.OrderStatus;
import com.uniclub.entity.enums.PaymentMethod;
import com.uniclub.entity.enums.PaymentStatus;
import com.uniclub.exception.ResourceNotFoundException;
import com.uniclub.repository.OrderRepository;
import com.uniclub.repository.PaymentRepository;
import com.uniclub.repository.UserRepository;
import com.uniclub.repository.VariantRepository;
import com.uniclub.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private VariantRepository variantRepository;
    @Mock
    private PaymentRepository paymentRepository;
    @Mock
    private CartService cartService;
    @Mock
    private VNPayService vnPayService;

    @InjectMocks
    private OrderServiceImpl orderService;

    private User user;
    private Variant variantA;
    private Variant variantB;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1);

        variantA = new Variant();
        variantA.setSku(10);
        variantA.setQuantity(20);

        variantB = new Variant();
        variantB.setSku(20);
        variantB.setQuantity(20);
    }

    @Test
    void createOrder_shouldApplyFreeShippingWhenSubtotalAboveThreshold() {
        CreateOrderRequest request = buildRequest(List.of(
                buildVariantRequest(variantA.getSku(), 1, 300_000),
                buildVariantRequest(variantB.getSku(), 1, 250_000)
        ));

        stubCommonRepositories();
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        OrderResponse response = orderService.createOrder(request);

        assertThat(response.getShippingFee()).isEqualTo(0);
        assertThat(response.getTotal()).isEqualTo(550_000);
        verify(cartService).clearCartByUserId(1);
    }

    @Test
    void createOrder_shouldAddShippingFeeWhenSubtotalBelowThreshold() {
        CreateOrderRequest request = buildRequest(List.of(
                buildVariantRequest(variantA.getSku(), 1, 200_000),
                buildVariantRequest(variantB.getSku(), 1, 100_000)
        ));

        stubCommonRepositories();
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        OrderResponse response = orderService.createOrder(request);

        assertThat(response.getShippingFee()).isEqualTo(30_000);
        assertThat(response.getTotal()).isEqualTo(330_000);
    }

    @Test
    void createOrder_shouldFailWhenVariantQuantityIsInsufficient() {
        variantA.setQuantity(1);
        CreateOrderRequest request = buildRequest(List.of(
                buildVariantRequest(variantA.getSku(), 5, 200_000)
        ));

        when(variantRepository.findById(variantA.getSku())).thenReturn(Optional.of(variantA));

        assertThatThrownBy(() -> orderService.createOrder(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("không đủ số lượng");

        verify(orderRepository, never()).save(any());
    }

    @Test
    void createOrder_shouldFailWhenShippingPhoneMissing() {
        CreateOrderRequest request = buildRequest(List.of(
                buildVariantRequest(variantA.getSku(), 1, 200_000)
        ));
        request.setRecipientPhone("   ");

        assertThatThrownBy(() -> orderService.createOrder(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Vui lòng nhập số điện thoại");
    }

    @Test
    void retryVNPayPayment_shouldReturnPaymentUrlForPendingOrder() {
        Order order = new Order();
        order.setId(99);
        order.setStatus(OrderStatus.PENDING);

        when(orderRepository.findById(99)).thenReturn(Optional.of(order));
        when(paymentRepository.findLatestByOrderId(99)).thenReturn(Optional.of(new Payment()));
        when(vnPayService.createPaymentUrl(any(), any(), any(), any())).thenReturn("http://pay");

        String url = orderService.retryVNPayPayment(99);

        assertThat(url).isEqualTo("http://pay");
        verify(vnPayService).createPaymentUrl(99, "Thanh toan lai don hang 99", "127.0.0.1", "vn");
    }

    @Test
    void retryVNPayPayment_shouldRejectCancelledOrder() {
        Order order = new Order();
        order.setId(77);
        order.setStatus(OrderStatus.CANCELLED);

        when(orderRepository.findById(77)).thenReturn(Optional.of(order));

        assertThatThrownBy(() -> orderService.retryVNPayPayment(77))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("đã bị hủy");
    }

    @Test
    void retryVNPayPayment_shouldRequirePendingStatus() {
        Order order = new Order();
        order.setId(50);
        order.setStatus(OrderStatus.CONFIRMED);

        when(orderRepository.findById(50)).thenReturn(Optional.of(order));

        assertThatThrownBy(() -> orderService.retryVNPayPayment(50))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("trạng thái PENDING");
    }

    // M3-01: Tạo đơn hàng thành công (thanh toán COD)
    @Test
    void createOrder_shouldCreateOrderWithCOD() {
        CreateOrderRequest request = buildRequest(List.of(
                buildVariantRequest(variantA.getSku(), 1, 300_000)
        ));
        request.setPaymentMethod(PaymentMethod.COD);

        stubCommonRepositories();
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order order = invocation.getArgument(0);
            order.setId(1);
            return order;
        });
        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        OrderResponse response = orderService.createOrder(request);

        assertThat(response.getStatus()).isEqualTo(OrderStatus.PENDING.name());
        verify(paymentRepository).save(any(Payment.class));
        verify(cartService).clearCartByUserId(1);
    }

    // M3-02: Tạo đơn hàng thành công (thanh toán VNPay)
    @Test
    void createOrder_shouldCreateOrderWithVNPay() {
        CreateOrderRequest request = buildRequest(List.of(
                buildVariantRequest(variantA.getSku(), 1, 300_000)
        ));
        request.setPaymentMethod(PaymentMethod.VNPay);

        stubCommonRepositories();
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order order = invocation.getArgument(0);
            order.setId(1);
            return order;
        });
        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        OrderResponse response = orderService.createOrder(request);

        assertThat(response.getStatus()).isEqualTo(OrderStatus.PENDING.name());
        verify(paymentRepository).save(any(Payment.class));
    }

    // M3-03: Tạo đơn hàng với giỏ hàng rỗng
    @Test
    void createOrder_shouldFailWhenOrderVariantsIsEmpty() {
        CreateOrderRequest request = buildRequest(List.of());
        // Note: This is handled at service level, not cart level

        assertThatThrownBy(() -> orderService.createOrder(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("ít nhất một sản phẩm");

        verify(orderRepository, never()).save(any());
    }

    // M3-04: Tạo đơn hàng thiếu thông tin billing
    @Test
    void createOrder_shouldFailWhenRecipientNameMissing() {
        CreateOrderRequest request = buildRequest(List.of(
                buildVariantRequest(variantA.getSku(), 1, 200_000)
        ));
        request.setRecipientName("   ");

        assertThatThrownBy(() -> orderService.createOrder(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Vui lòng nhập tên người nhận");
    }

    @Test
    void createOrder_shouldFailWhenShippingAddressMissing() {
        CreateOrderRequest request = buildRequest(List.of(
                buildVariantRequest(variantA.getSku(), 1, 200_000)
        ));
        request.setShippingAddress("   ");

        assertThatThrownBy(() -> orderService.createOrder(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Vui lòng nhập địa chỉ giao hàng");
    }

    // M3-05: Tạo đơn hàng khi sản phẩm hết hàng (Race Condition)
    @Test
    void createOrder_shouldFailWhenVariantOutOfStock() {
        variantA.setQuantity(0);
        CreateOrderRequest request = buildRequest(List.of(
                buildVariantRequest(variantA.getSku(), 1, 200_000)
        ));

        when(variantRepository.findById(variantA.getSku())).thenReturn(Optional.of(variantA));

        assertThatThrownBy(() -> orderService.createOrder(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("không đủ số lượng");

        verify(orderRepository, never()).save(any());
    }

    // M3-06: Hủy đơn hàng (User)
    @Test
    void cancelOrder_shouldCancelPendingOrder() {
        Order order = new Order();
        order.setId(1);
        order.setUser(user);
        order.setStatus(OrderStatus.PENDING);

        OrderVariant orderVariant = new OrderVariant();
        orderVariant.setVariant(variantA);
        orderVariant.setQuantity(2);
        order.setOrderVariants(List.of(orderVariant));

        variantA.setQuantity(5); // After cancellation, should be restored

        Payment payment = new Payment();
        payment.setPaymentStatus(PaymentStatus.PENDING);

        when(orderRepository.findById(1)).thenReturn(Optional.of(order));
        when(paymentRepository.findByOrderId(1)).thenReturn(List.of(payment));
        when(variantRepository.findById(variantA.getSku())).thenReturn(Optional.of(variantA));
        when(variantRepository.save(any(Variant.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        OrderResponse response = orderService.cancelOrder(1);

        assertThat(response.getStatus()).isEqualTo(OrderStatus.CANCELLED.name());
        verify(variantRepository).save(variantA);
        assertThat(variantA.getQuantity()).isEqualTo(7); // 5 + 2 restored
    }

    @Test
    void cancelOrder_shouldThrowWhenOrderIsShipping() {
        Order order = new Order();
        order.setId(1);
        order.setStatus(OrderStatus.SHIPPING);

        when(orderRepository.findById(1)).thenReturn(Optional.of(order));

        assertThatThrownBy(() -> orderService.cancelOrder(1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("đang được giao");
    }

    @Test
    void cancelOrder_shouldThrowWhenOrderIsDelivered() {
        Order order = new Order();
        order.setId(1);
        order.setStatus(OrderStatus.DELIVERED);

        when(orderRepository.findById(1)).thenReturn(Optional.of(order));

        assertThatThrownBy(() -> orderService.cancelOrder(1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("đã hoàn thành");
    }

    @Test
    void cancelOrder_shouldThrowWhenOrderAlreadyCancelled() {
        Order order = new Order();
        order.setId(1);
        order.setStatus(OrderStatus.CANCELLED);

        when(orderRepository.findById(1)).thenReturn(Optional.of(order));

        assertThatThrownBy(() -> orderService.cancelOrder(1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("đã được hủy");
    }

    @Test
    void createOrder_shouldDeductVariantQuantity() {
        int initialQuantity = variantA.getQuantity();
        CreateOrderRequest request = buildRequest(List.of(
                buildVariantRequest(variantA.getSku(), 2, 200_000)
        ));

        stubCommonRepositories();
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        orderService.createOrder(request);

        verify(variantRepository).save(variantA);
        assertThat(variantA.getQuantity()).isEqualTo(initialQuantity - 2);
    }

    @Test
    void getOrderById_shouldReturnOrder() {
        Order order = new Order();
        order.setId(1);
        order.setUser(user);
        order.setStatus(OrderStatus.PENDING);

        when(orderRepository.findById(1)).thenReturn(Optional.of(order));
        when(paymentRepository.findByOrderId(1)).thenReturn(List.of());

        OrderResponse response = orderService.getOrderById(1);

        assertThat(response.getId()).isEqualTo(1);
    }

    @Test
    void getOrderById_shouldThrowWhenNotFound() {
        when(orderRepository.findById(999)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.getOrderById(999))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    private void stubCommonRepositories() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(variantRepository.findById(variantA.getSku())).thenReturn(Optional.of(variantA));
        when(variantRepository.findById(variantB.getSku())).thenReturn(Optional.of(variantB));
        when(variantRepository.save(any(Variant.class))).thenAnswer(invocation -> invocation.getArgument(0));
    }

    private CreateOrderRequest buildRequest(List<CreateOrderVariantRequest> variants) {
        CreateOrderRequest request = new CreateOrderRequest();
        request.setUserId(user.getId());
        request.setOrderVariants(variants);
        request.setRecipientName("Buyer");
        request.setRecipientPhone("0123456789");
        request.setShippingAddress("123 Street");
        request.setPaymentMethod(PaymentMethod.COD);
        return request;
    }

    private CreateOrderVariantRequest buildVariantRequest(int sku, int quantity, int price) {
        CreateOrderVariantRequest variantRequest = new CreateOrderVariantRequest();
        variantRequest.setVariantSku(sku);
        variantRequest.setQuantity(quantity);
        variantRequest.setPrice(price);
        return variantRequest;
    }

    // Additional edge case tests
    @Test
    void createOrder_shouldHandleMultipleVariantsWithDifferentQuantities() {
        CreateOrderRequest request = buildRequest(List.of(
                buildVariantRequest(variantA.getSku(), 3, 200_000),
                buildVariantRequest(variantB.getSku(), 2, 150_000)
        ));

        stubCommonRepositories();
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        OrderResponse response = orderService.createOrder(request);

        assertThat(response.getTotal()).isEqualTo(900_000 + 30_000); // (3*200k + 2*150k) + shipping
        verify(variantRepository).save(variantA);
        verify(variantRepository).save(variantB);
        assertThat(variantA.getQuantity()).isEqualTo(17); // 20 - 3
        assertThat(variantB.getQuantity()).isEqualTo(18); // 20 - 2
    }

    @Test
    void createOrder_shouldFailWhenOneVariantOutOfStock() {
        variantA.setQuantity(0);
        CreateOrderRequest request = buildRequest(List.of(
                buildVariantRequest(variantA.getSku(), 1, 200_000),
                buildVariantRequest(variantB.getSku(), 1, 150_000)
        ));

        when(variantRepository.findById(variantA.getSku())).thenReturn(Optional.of(variantA));

        assertThatThrownBy(() -> orderService.createOrder(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("không đủ số lượng");

        verify(orderRepository, never()).save(any());
    }

    @Test
    void createOrder_shouldFailWhenUserIdIsNull() {
        CreateOrderRequest request = buildRequest(List.of(
                buildVariantRequest(variantA.getSku(), 1, 200_000)
        ));
        request.setUserId(null);

        assertThatThrownBy(() -> orderService.createOrder(request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("User");
    }

    @Test
    void createOrder_shouldFailWhenVariantNotFound() {
        CreateOrderRequest request = buildRequest(List.of(
                buildVariantRequest(999, 1, 200_000)
        ));

        stubCommonRepositories();
        when(variantRepository.findById(999)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.createOrder(request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Variant");
    }

    @Test
    void cancelOrder_shouldRestoreMultipleVariantQuantities() {
        Order order = new Order();
        order.setId(1);
        order.setUser(user);
        order.setStatus(OrderStatus.PENDING);

        OrderVariant orderVariant1 = new OrderVariant();
        orderVariant1.setVariant(variantA);
        orderVariant1.setQuantity(3);

        OrderVariant orderVariant2 = new OrderVariant();
        orderVariant2.setVariant(variantB);
        orderVariant2.setQuantity(2);

        order.setOrderVariants(List.of(orderVariant1, orderVariant2));

        variantA.setQuantity(5);
        variantB.setQuantity(8);

        Payment payment = new Payment();
        payment.setPaymentStatus(PaymentStatus.PENDING);

        when(orderRepository.findById(1)).thenReturn(Optional.of(order));
        when(paymentRepository.findByOrderId(1)).thenReturn(List.of(payment));
        when(variantRepository.findById(variantA.getSku())).thenReturn(Optional.of(variantA));
        when(variantRepository.findById(variantB.getSku())).thenReturn(Optional.of(variantB));
        when(variantRepository.save(any(Variant.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        OrderResponse response = orderService.cancelOrder(1);

        assertThat(response.getStatus()).isEqualTo(OrderStatus.CANCELLED.name());
        assertThat(variantA.getQuantity()).isEqualTo(8); // 5 + 3 restored
        assertThat(variantB.getQuantity()).isEqualTo(10); // 8 + 2 restored
    }

    @Test
    void createOrder_shouldCalculateShippingFeeCorrectlyAtBoundary() {
        // Test at exactly 499,000 (should be free shipping)
        CreateOrderRequest request = buildRequest(List.of(
                buildVariantRequest(variantA.getSku(), 1, 499_000)
        ));

        stubCommonRepositories();
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        OrderResponse response = orderService.createOrder(request);

        assertThat(response.getShippingFee()).isEqualTo(0);
        assertThat(response.getTotal()).isEqualTo(499_000);
    }

    @Test
    void createOrder_shouldCalculateShippingFeeCorrectlyBelowBoundary() {
        // Test at 498,999 (should have shipping fee)
        CreateOrderRequest request = buildRequest(List.of(
                buildVariantRequest(variantA.getSku(), 1, 498_999)
        ));

        stubCommonRepositories();
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        OrderResponse response = orderService.createOrder(request);

        assertThat(response.getShippingFee()).isEqualTo(30_000);
        assertThat(response.getTotal()).isEqualTo(528_999);
    }
}

