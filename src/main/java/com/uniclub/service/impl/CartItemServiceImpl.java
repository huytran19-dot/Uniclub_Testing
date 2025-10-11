package com.uniclub.service.impl;

import com.uniclub.dto.request.CartItem.CreateCartItemRequest;
import com.uniclub.dto.request.CartItem.UpdateCartItemRequest;
import com.uniclub.dto.response.CartItem.CartItemResponse;
import com.uniclub.entity.Cart;
import com.uniclub.entity.CartItem;
import com.uniclub.entity.Variant;
import com.uniclub.exception.ResourceNotFoundException;
import com.uniclub.repository.CartItemRepository;
import com.uniclub.repository.CartRepository;
import com.uniclub.repository.VariantRepository;
import com.uniclub.service.CartItemService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class CartItemServiceImpl implements CartItemService {
    @Autowired
    private CartItemRepository cartItemRepository;
    
    @Autowired
    private CartRepository cartRepository;
    
    @Autowired
    private VariantRepository variantRepository;

    @Override
    public CartItemResponse createCartItem(CreateCartItemRequest request) {
        // Check if cart exists
        Cart cart = cartRepository.findById(request.getCartId())
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "id", request.getCartId()));

        // Check if variant exists
        Variant variant = variantRepository.findById(request.getVariantSku())
                .orElseThrow(() -> new ResourceNotFoundException("Variant", "sku", request.getVariantSku()));

        // Check if cart item with same variant already exists
        if (cartItemRepository.existsByCartIdAndVariantSku(request.getCartId(), request.getVariantSku())) {
            throw new IllegalArgumentException("Sản phẩm này đã có trong giỏ hàng");
        }

        // Check if variant has enough quantity
        if (variant.getQuantity() < request.getQuantity()) {
            throw new IllegalArgumentException("Số lượng sản phẩm không đủ");
        }

        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setVariant(variant);
        cartItem.setQuantity(request.getQuantity());
        cartItem.setUnitPrice(request.getUnitPrice());

        CartItem savedCartItem = cartItemRepository.save(cartItem);
        return CartItemResponse.fromEntity(savedCartItem);
    }

    @Override
    public CartItemResponse updateCartItem(Integer cartItemId, UpdateCartItemRequest request) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("CartItem", "id", cartItemId));

        if (request.getQuantity() != null) {
            // Check if variant has enough quantity
            if (cartItem.getVariant().getQuantity() < request.getQuantity()) {
                throw new IllegalArgumentException("Số lượng sản phẩm không đủ");
            }
            cartItem.setQuantity(request.getQuantity());
        }
        if (request.getUnitPrice() != null) {
            cartItem.setUnitPrice(request.getUnitPrice());
        }

        CartItem updatedCartItem = cartItemRepository.save(cartItem);
        return CartItemResponse.fromEntity(updatedCartItem);
    }

    @Override
    public List<CartItemResponse> getAllCartItems() {
        return cartItemRepository.findAll()
                .stream()
                .map(CartItemResponse::fromEntity)
                .toList();
    }

    @Override
    public CartItemResponse getCartItemById(Integer cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("CartItem", "id", cartItemId));
        return CartItemResponse.fromEntity(cartItem);
    }

    @Override
    public List<CartItemResponse> getCartItemsByCartId(Integer cartId) {
        return cartItemRepository.findByCartId(cartId)
                .stream()
                .map(CartItemResponse::fromEntity)
                .toList();
    }

    // Hard delete
    @Override
    public void deleteCartItem(Integer cartItemId) {
        if (!cartItemRepository.existsById(cartItemId)) {
            throw new ResourceNotFoundException("CartItem", "id", cartItemId);
        }
        cartItemRepository.deleteById(cartItemId);
    }
}
