package com.uniclub.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Data
@Entity(name = "variant")
public class Variant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int sku;

    private String images;
    private int quantity;
    private double price;

    @CreationTimestamp
    @Column(name="create_date")
    private Date createdAt;
}
