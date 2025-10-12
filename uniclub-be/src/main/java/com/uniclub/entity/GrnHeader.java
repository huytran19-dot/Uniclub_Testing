package com.uniclub.entity;

import com.uniclub.entity.enums.GrnStatus;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "grn_header")
public class GrnHeader {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "total_cost", nullable = false, columnDefinition = "INT DEFAULT 0")
    private Integer totalCost = 0;

    @Column(name = "note", length = 255)
    private String note;

    @Column(name = "received_date", columnDefinition = "DATE DEFAULT (CURRENT_DATE)")
    private LocalDate receivedDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "ENUM('PENDING','COMPLETED','CANCELLED') DEFAULT 'PENDING'")
    private GrnStatus status = GrnStatus.PENDING;

    @Column(name = "created_at", nullable = false,
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false,
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt;

    // Quan hệ N-1 với Supplier
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_supplier", nullable = false)
    private Supplier supplier;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.receivedDate == null) {
            this.receivedDate = LocalDate.now();
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
