package com.uniclub.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@Entity(name = "color")
@Data
public class Color {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(name = "hex_code", length = 7)
    private String hexCode;

    @Builder.Default
    private Byte status = 1;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;

    // Quan hệ 1-n: 1 color có thể thuộc nhiều variant
    @OneToMany(mappedBy = "color", fetch = FetchType.LAZY)
    private List<Variant> variants;
}
