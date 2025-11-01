package com.uniclub.entity;

import java.time.LocalDateTime;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "variant")
public class Variant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer sku;

    @Column(name = "images", length = 255)
    private String images;

    @Column(name = "quantity", columnDefinition = "INT DEFAULT 0")
    private Integer quantity = 0;

    @Column(name = "price")
    private Integer price;

    @Column(name = "status", columnDefinition = "TINYINT DEFAULT 1")
    private Byte status = 1;

    @Column(name = "created_at", nullable = false, updatable = false,
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false,
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
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

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
