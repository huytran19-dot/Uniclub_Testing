package com.uniclub.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity(name = "size")
public class Size {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    // 1 = active, 0 = inactive
    @Column(columnDefinition = "TINYINT DEFAULT 1")
    private Integer status = 1;

    // Dùng default CURRENT_TIMESTAMP từ DB
    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    // Dùng ON UPDATE CURRENT_TIMESTAMP từ DB
    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;
}
