package com.uniclub.service;

import com.uniclub.dto.request.CartItem.CreateCartItemRequest;
import com.uniclub.dto.response.CartItem.CartItemResponse;
import com.uniclub.entity.Cart;
import com.uniclub.entity.CartItem;
import com.uniclub.entity.Variant;
import com.uniclub.repository.CartItemRepository;
import com.uniclub.repository.CartRepository;
import com.uniclub.repository.VariantRepository;
import com.uniclub.service.impl.CartItemServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CartItemServiceImplTest {

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private VariantRepository variantRepository;

    @InjectMocks
    private CartItemServiceImpl cartItemService;

    @Test
    void createCartItem_shouldIncreaseQuantityWhenItemAlreadyExists() {
        Cart cart = new Cart();
        cart.setId(1);

        Variant variant = new Variant();
        variant.setSku(100);
        variant.setQuantity(10);

        CartItem existingCartItem = new CartItem();
        existingCartItem.setCart(cart);
        existingCartItem.setVariant(variant);
        existingCartItem.setQuantity(1);

        CreateCartItemRequest request = new CreateCartItemRequest();
        request.setCartId(1);
        request.setVariantSku(100);
        request.setQuantity(2);
        request.setUnitPrice(120_000);

        when(cartRepository.findById(1)).thenReturn(Optional.of(cart));
        when(variantRepository.findById(100)).thenReturn(Optional.of(variant));
        when(cartItemRepository.findByCartIdAndVariantSku(1, 100)).thenReturn(Optional.of(existingCartItem));
        when(cartItemRepository.save(existingCartItem)).thenAnswer(invocation -> invocation.getArgument(0));

        CartItemResponse response = cartItemService.createCartItem(request);

        assertThat(response.getQuantity()).isEqualTo(3);
        verify(cartItemRepository).save(existingCartItem);
    }

    @Test
    void createCartItem_shouldThrowWhenRequestedQuantityExceedsInventory() {
        Cart cart = new Cart();
        cart.setId(1);

        Variant variant = new Variant();
        variant.setSku(200);
        variant.setQuantity(2);

        CreateCartItemRequest request = new CreateCartItemRequest();
        request.setCartId(1);
        request.setVariantSku(200);
        request.setQuantity(5);
        request.setUnitPrice(150_000);

        when(cartRepository.findById(1)).thenReturn(Optional.of(cart));
        when(variantRepository.findById(200)).thenReturn(Optional.of(variant));
        when(cartItemRepository.findByCartIdAndVariantSku(1, 200)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> cartItemService.createCartItem(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Số lượng sản phẩm không đủ");

        verify(cartItemRepository).findByCartIdAndVariantSku(1, 200);
    }
}

