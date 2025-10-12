package com.uniclub.service;

import com.uniclub.dto.request.CartItem.CreateCartItemRequest;
import com.uniclub.dto.request.CartItem.UpdateCartItemRequest;
import com.uniclub.dto.response.CartItem.CartItemResponse;

import java.util.List;

public interface CartItemService {
    CartItemResponse createCartItem(CreateCartItemRequest request);
    CartItemResponse updateCartItem(Integer cartItemId, UpdateCartItemRequest request);
    void deleteCartItem(Integer id);

    List<CartItemResponse> getAllCartItems();
    CartItemResponse getCartItemById(Integer id);
    List<CartItemResponse> getCartItemsByCartId(Integer cartId);
}
