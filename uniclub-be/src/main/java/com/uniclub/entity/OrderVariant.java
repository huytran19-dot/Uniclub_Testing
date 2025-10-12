package com.uniclub.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "order_variant")
@IdClass(OrderVariantId.class) // composite key
public class OrderVariant {
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_order", foreignKey = @ForeignKey(name = "FK_ov_order"))
    private Order order;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sku_variant", foreignKey = @ForeignKey(name = "FK_ov_variant"))
    private Variant variant;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private Integer price;
}


