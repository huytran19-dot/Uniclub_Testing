package com.uniclub.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "grn_detail")
public class GrnDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "unit_cost", nullable = false)
    private Integer unitCost;

    // subtotal = quantity * unit_cost → Generated column, read-only
    @Column(name = "subtotal", insertable = false, updatable = false)
    private Integer subtotal;

    // Quan hệ N-1 với GrnHeader
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_grn", nullable = false)
    private GrnHeader grnHeader;

    // Quan hệ N-1 với Variant
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_variant", referencedColumnName = "sku", nullable = false)
    private Variant variant;
}
