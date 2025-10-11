package com.uniclub.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "cart_item")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "quantity")
    private Integer quantity = 1;

    @Column(name = "unit_price", nullable = false)
    private Integer unitPrice;

    // subtotal = quantity * unit_price → cột generated trong DB
    // JPA không hỗ trợ tính toán tự động cột GENERATED, nên đánh dấu là read-only
    @Column(name = "subtotal", insertable = false, updatable = false)
    private Integer subtotal;

    @Column(name = "added_at", nullable = false,
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime addedAt;

    // Quan hệ N-1 với Cart
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cart", nullable = false)
    private Cart cart;

    // Quan hệ N-1 với Variant (sản phẩm cụ thể)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_sku", referencedColumnName = "sku", nullable = false)
    private Variant variant;

    @PrePersist
    public void prePersist() {
        this.addedAt = LocalDateTime.now();
    }
}
