package com.uniclub.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uniclub.dto.request.Cart.CreateCartRequest;
import com.uniclub.dto.request.Cart.UpdateCartRequest;
import com.uniclub.dto.response.Cart.CartResponse;
import com.uniclub.entity.Cart;
import com.uniclub.entity.User;
import com.uniclub.exception.ResourceNotFoundException;
import com.uniclub.repository.CartRepository;
import com.uniclub.repository.UserRepository;
import com.uniclub.service.CartService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CartServiceImpl implements CartService {
    @Autowired
    private CartRepository cartRepository;
    
    @Autowired
    private UserRepository userRepository;

    @Override
    public CartResponse createCart(CreateCartRequest request) {
        // Check if user exists
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", request.getUserId()));

        // Check if user already has a cart
        if (cartRepository.existsByUserId(request.getUserId())) {
            throw new IllegalArgumentException("Người dùng này đã có giỏ hàng");
        }

        Cart cart = new Cart();
        cart.setUser(user);
        cart.setNote(request.getNote());
        cart.setTotalPrice(0);
        cart.setShippingFee(0);

        Cart savedCart = cartRepository.save(cart);
        return CartResponse.fromEntity(savedCart);
    }

    @Override
    public CartResponse updateCart(Integer cartId, UpdateCartRequest request) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "id", cartId));

        if (request.getNote() != null) {
            cart.setNote(request.getNote());
        }
        if (request.getStatus() != null) {
            cart.setStatus(request.getStatus());
        }

        Cart updatedCart = cartRepository.save(cart);
        return CartResponse.fromEntity(updatedCart);
    }

    @Override
    public List<CartResponse> getAllCarts() {
        // ✅ Dùng query với JOIN FETCH
        List<Cart> carts = cartRepository.findAllWithUsers();
        
        return carts.stream()
                .map(CartResponse::fromEntity)
                .toList();
    }

    @Override
    public CartResponse getCartById(Integer cartId) {
        // ✅ Dùng findByIdWithUser để eager load User
        Cart cart = cartRepository.findByIdWithUser(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "id", cartId));
        
        return CartResponse.fromEntity(cart);
    }

    @Override
    public CartResponse getCartByUserId(Integer userId) {
        // ✅ findByUserId đã có JOIN FETCH rồi
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "userId", userId));
        
        return CartResponse.fromEntity(cart);
    }

    // Hard delete
    @Override
    public void deleteCart(Integer cartId) {
        if (!cartRepository.existsById(cartId)) {
            throw new ResourceNotFoundException("Cart", "id", cartId);
        }
        cartRepository.deleteById(cartId);
    }
    
    // Clear all cart items for a user
    @Override
    public void clearCartByUserId(Integer userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "userId", userId));
        cart.getCartItems().clear();
        cartRepository.save(cart);
    }
}
