package com.uniclub.controller;

import com.uniclub.dto.request.Cart.CreateCartRequest;
import com.uniclub.dto.request.Cart.UpdateCartRequest;
import com.uniclub.dto.response.Cart.CartResponse;
import com.uniclub.service.CartService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/carts")
@Validated
@CrossOrigin(origins = "*")
public class CartController {

    @Autowired
    private CartService cartService;

    // CREATE
    @PostMapping
    public ResponseEntity<CartResponse> create(@Valid @RequestBody CreateCartRequest request) {
        CartResponse created = cartService.createCart(request);
        return ResponseEntity
                .created(URI.create("/api/carts/" + created.getId()))
                .body(created);
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<CartResponse> update(@PathVariable Integer id,
                                                @Valid @RequestBody UpdateCartRequest request) {
        CartResponse updated = cartService.updateCart(id, request);
        return ResponseEntity.ok(updated);
    }

    // GET ALL
    @GetMapping
    public ResponseEntity<List<CartResponse>> getAll() {
        return ResponseEntity.ok(cartService.getAllCarts());
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<CartResponse> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(cartService.getCartById(id));
    }

    // GET BY USER ID
    @GetMapping("/user/{userId}")
    public ResponseEntity<CartResponse> getByUserId(@PathVariable Integer userId) {
        return ResponseEntity.ok(cartService.getCartByUserId(userId));
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        cartService.deleteCart(id);
        return ResponseEntity.noContent().build();
    }
}
