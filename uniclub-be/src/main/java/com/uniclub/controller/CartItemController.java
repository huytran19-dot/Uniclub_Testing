package com.uniclub.controller;

import com.uniclub.dto.request.CartItem.CreateCartItemRequest;
import com.uniclub.dto.request.CartItem.UpdateCartItemRequest;
import com.uniclub.dto.response.CartItem.CartItemResponse;
import com.uniclub.service.CartItemService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/cart-items")
@Validated
@CrossOrigin(origins = "*")
public class CartItemController {

    @Autowired
    private CartItemService cartItemService;

    // CREATE
    @PostMapping
    public ResponseEntity<CartItemResponse> create(@Valid @RequestBody CreateCartItemRequest request) {
        CartItemResponse created = cartItemService.createCartItem(request);
        return ResponseEntity
                .created(URI.create("/api/cart-items/" + created.getId()))
                .body(created);
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<CartItemResponse> update(@PathVariable Integer id,
                                                @Valid @RequestBody UpdateCartItemRequest request) {
        CartItemResponse updated = cartItemService.updateCartItem(id, request);
        return ResponseEntity.ok(updated);
    }

    // GET ALL
    @GetMapping
    public ResponseEntity<List<CartItemResponse>> getAll() {
        return ResponseEntity.ok(cartItemService.getAllCartItems());
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<CartItemResponse> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(cartItemService.getCartItemById(id));
    }

    // GET BY CART ID
    @GetMapping("/cart/{cartId}")
    public ResponseEntity<List<CartItemResponse>> getByCartId(@PathVariable Integer cartId) {
        return ResponseEntity.ok(cartItemService.getCartItemsByCartId(cartId));
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        cartItemService.deleteCartItem(id);
        return ResponseEntity.noContent().build();
    }
}
