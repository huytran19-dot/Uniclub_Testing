package com.uniclub.service;

import com.uniclub.dto.request.Order.CreateOrderRequest;
import com.uniclub.dto.request.Order.CreateOrderVariantRequest;
import com.uniclub.dto.response.Order.OrderResponse;
import com.uniclub.entity.Order;
import com.uniclub.entity.Payment;
import com.uniclub.entity.User;
import com.uniclub.entity.Variant;
import com.uniclub.entity.enums.OrderStatus;
import com.uniclub.entity.enums.PaymentMethod;
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
}

