package com.uniclub.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import java.util.List;
import java.util.ArrayList;

import java.time.LocalDateTime;

@Builder
@Data
@Entity(name = "brand")
public class Brand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 50)
    private String name;

    @Builder.Default
    private Byte status = 1;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "brand", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Product> products = new ArrayList<>();

    // đồng bộ 2 chiều
    public void addProduct(Product p) {
        products.add(p);
        p.setBrand(this);
    }

    public void removeProduct(Product p) {
        products.remove(p);
        p.setBrand(null);
    }
}
