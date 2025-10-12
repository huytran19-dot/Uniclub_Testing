package com.uniclub.service;

import com.uniclub.dto.request.Cart.CreateCartRequest;
import com.uniclub.dto.request.Cart.UpdateCartRequest;
import com.uniclub.dto.response.Cart.CartResponse;

import java.util.List;

public interface CartService {
    CartResponse createCart(CreateCartRequest request);
    CartResponse updateCart(Integer cartId, UpdateCartRequest request);
    void deleteCart(Integer id);

    List<CartResponse> getAllCarts();
    CartResponse getCartById(Integer id);
    CartResponse getCartByUserId(Integer userId);
}
