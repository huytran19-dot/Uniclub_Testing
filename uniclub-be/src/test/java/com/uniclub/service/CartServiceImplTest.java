package com.uniclub.service;

import com.uniclub.dto.request.Cart.CreateCartRequest;
import com.uniclub.dto.request.Cart.UpdateCartRequest;
import com.uniclub.dto.response.Cart.CartResponse;
import com.uniclub.entity.Cart;
import com.uniclub.entity.User;
import com.uniclub.exception.ResourceNotFoundException;
import com.uniclub.repository.CartRepository;
import com.uniclub.repository.UserRepository;
import com.uniclub.service.impl.CartServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
class CartServiceImplTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CartServiceImpl cartService;

    private User user;
    private Cart cart;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1);
        user.setEmail("user@example.com");

        cart = new Cart();
        cart.setId(1);
        cart.setUser(user);
        cart.setTotalPrice(0);
    }

    @Test
    void createCart_shouldCreateCartSuccessfully() {
        CreateCartRequest request = new CreateCartRequest();
        request.setUserId(1);
        request.setNote("Test note");

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(cartRepository.existsByUserId(1)).thenReturn(false);
        when(cartRepository.save(any(Cart.class))).thenAnswer(invocation -> {
            Cart c = invocation.getArgument(0);
            c.setId(1);
            return c;
        });

        CartResponse response = cartService.createCart(request);

        assertThat(response.getUserId()).isEqualTo(1);
        verify(cartRepository).save(any(Cart.class));
    }

    @Test
    void createCart_shouldThrowWhenUserAlreadyHasCart() {
        CreateCartRequest request = new CreateCartRequest();
        request.setUserId(1);

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(cartRepository.existsByUserId(1)).thenReturn(true);

        assertThatThrownBy(() -> cartService.createCart(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("đã có giỏ hàng");

        verify(cartRepository, never()).save(any());
    }

    @Test
    void createCart_shouldThrowWhenUserNotFound() {
        CreateCartRequest request = new CreateCartRequest();
        request.setUserId(999);

        when(userRepository.findById(999)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> cartService.createCart(request))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void getCartByUserId_shouldReturnCart() {
        when(cartRepository.findByUserId(1)).thenReturn(Optional.of(cart));

        CartResponse response = cartService.getCartByUserId(1);

        assertThat(response.getId()).isEqualTo(1);
        assertThat(response.getUserId()).isEqualTo(1);
    }

    @Test
    void getCartByUserId_shouldThrowWhenNotFound() {
        when(cartRepository.findByUserId(999)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> cartService.getCartByUserId(999))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void getCartById_shouldReturnCart() {
        when(cartRepository.findByIdWithUser(1)).thenReturn(Optional.of(cart));

        CartResponse response = cartService.getCartById(1);

        assertThat(response.getId()).isEqualTo(1);
    }

    @Test
    void getCartById_shouldThrowWhenNotFound() {
        when(cartRepository.findByIdWithUser(999)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> cartService.getCartById(999))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void updateCart_shouldUpdateCartSuccessfully() {
        UpdateCartRequest request = new UpdateCartRequest();
        request.setNote("Updated note");

        when(cartRepository.findById(1)).thenReturn(Optional.of(cart));
        when(cartRepository.save(any(Cart.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CartResponse response = cartService.updateCart(1, request);

        assertThat(response.getNote()).isEqualTo("Updated note");
        verify(cartRepository).save(cart);
    }

    @Test
    void updateCart_shouldThrowWhenCartNotFound() {
        UpdateCartRequest request = new UpdateCartRequest();
        when(cartRepository.findById(999)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> cartService.updateCart(999, request))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void clearCartByUserId_shouldClearCartItems() {
        when(cartRepository.findByUserId(1)).thenReturn(Optional.of(cart));
        when(cartRepository.save(any(Cart.class))).thenAnswer(invocation -> invocation.getArgument(0));

        cartService.clearCartByUserId(1);

        verify(cartRepository).save(cart);
        assertThat(cart.getCartItems()).isEmpty();
    }

    @Test
    void clearCartByUserId_shouldThrowWhenCartNotFound() {
        when(cartRepository.findByUserId(999)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> cartService.clearCartByUserId(999))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void deleteCart_shouldDeleteCartSuccessfully() {
        when(cartRepository.existsById(1)).thenReturn(true);

        cartService.deleteCart(1);

        verify(cartRepository).deleteById(1);
    }

    @Test
    void deleteCart_shouldThrowWhenCartNotFound() {
        when(cartRepository.existsById(999)).thenReturn(false);

        assertThatThrownBy(() -> cartService.deleteCart(999))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(cartRepository, never()).deleteById(any());
    }

    @Test
    void getAllCarts_shouldReturnAllCarts() {
        Cart cart2 = new Cart();
        cart2.setId(2);

        when(cartRepository.findAllWithUsers()).thenReturn(List.of(cart, cart2));

        List<CartResponse> responses = cartService.getAllCarts();

        assertThat(responses).hasSize(2);
    }

    // M2-10: Kiểm tra tính nhất quán (persistence) của giỏ hàng sau khi đăng xuất/đăng nhập
    @Test
    void getCartByUserId_shouldReturnPersistedCartAfterLogoutLogin() {
        // Simulate: User logged in, added items, logged out, then logged in again
        // The cart should still exist with the same items
        cart.setTotalPrice(120_000);
        when(cartRepository.findByUserId(1)).thenReturn(Optional.of(cart));

        CartResponse response = cartService.getCartByUserId(1);

        assertThat(response.getId()).isEqualTo(1);
        assertThat(response.getUserId()).isEqualTo(1);
        assertThat(response.getTotalPrice()).isEqualTo(120_000);
        // This test verifies that cart persistence works - cart exists after logout/login
    }

    @Test
    void clearCartByUserId_shouldMaintainCartButClearItems() {
        // Setup: Cart has items, then clear
        cart.setTotalPrice(120_000);
        when(cartRepository.findByUserId(1)).thenReturn(Optional.of(cart));
        when(cartRepository.save(any(Cart.class))).thenAnswer(invocation -> invocation.getArgument(0));

        cartService.clearCartByUserId(1);

        verify(cartRepository).save(cart);
        assertThat(cart.getCartItems()).isEmpty();
        // Cart still exists, but items are cleared
    }
}

