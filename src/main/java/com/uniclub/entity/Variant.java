package com.uniclub.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Entity (name = "variant")
@Builder
public class Variant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer sku;

    @Column(name = "images", length = 255)
    private String images;

    @Column(name = "quantity", columnDefinition = "INT DEFAULT 0")
    private Integer quantity;

    @Column(name = "price")
    private Integer price;

    @Column(name = "status", columnDefinition = "TINYINT DEFAULT 1")
    private Byte status;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_product", nullable = false,
            foreignKey = @ForeignKey(name = "FK_variant_product"))
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_size",
            foreignKey = @ForeignKey(name = "FK_variant_size"))
    private Size size;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_color",
            foreignKey = @ForeignKey(name = "FK_variant_color"))
    private Color color;

    @OneToMany(mappedBy = "variant", fetch = FetchType.LAZY)
    private Set<OrderVariant> orderVariants;



}
