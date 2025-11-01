package com.uniclub.controller;

import com.uniclub.dto.request.Order.CreateOrderVariantRequest;
import com.uniclub.dto.response.Order.OrderVariantResponse;
import com.uniclub.service.OrderVariantService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/order-variants")
@Validated
@CrossOrigin(origins = "*")
public class OrderVariantController {

    @Autowired
    private OrderVariantService orderVariantService;

    // CREATE
    @PostMapping("/{orderId}")
    public ResponseEntity<OrderVariantResponse> create(@PathVariable Integer orderId,
                                                     @Valid @RequestBody CreateOrderVariantRequest request) {
        OrderVariantResponse created = orderVariantService.createOrderVariant(orderId, request);
        return ResponseEntity
                .created(URI.create("/api/order-variants/" + orderId + "/" + created.getVariantSku()))
                .body(created);
    }

    // UPDATE
    @PutMapping("/{orderId}/{variantSku}")
    public ResponseEntity<OrderVariantResponse> update(@PathVariable Integer orderId,
                                                      @PathVariable Integer variantSku,
                                                      @RequestParam(required = false) Integer quantity,
                                                      @RequestParam(required = false) Integer price) {
        OrderVariantResponse updated = orderVariantService.updateOrderVariant(orderId, variantSku, quantity, price);
        return ResponseEntity.ok(updated);
    }

    // GET ALL
    @GetMapping
    public ResponseEntity<List<OrderVariantResponse>> getAll() {
        return ResponseEntity.ok(orderVariantService.getAllOrderVariants());
    }

    // GET BY ID
    @GetMapping("/{orderId}/{variantSku}")
    public ResponseEntity<OrderVariantResponse> getById(@PathVariable Integer orderId,
                                                        @PathVariable Integer variantSku) {
        return ResponseEntity.ok(orderVariantService.getOrderVariantById(orderId, variantSku));
    }

    // GET BY ORDER ID
    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<OrderVariantResponse>> getByOrderId(@PathVariable Integer orderId) {
        return ResponseEntity.ok(orderVariantService.getOrderVariantsByOrderId(orderId));
    }

    // DELETE
    @DeleteMapping("/{orderId}/{variantSku}")
    public ResponseEntity<Void> delete(@PathVariable Integer orderId,
                                      @PathVariable Integer variantSku) {
        orderVariantService.deleteOrderVariant(orderId, variantSku);
        return ResponseEntity.noContent().build();
    }
}
