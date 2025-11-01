package com.uniclub.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Data
@Entity(name = "review")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // SKU kept as regular column (not auto-generated) to avoid multiple AUTO_INCREMENT columns
    private Integer sku;

    private double star;
    private String content;

    @CreationTimestamp
    @Column(name = "create_date")
    private Date createdAt;

    private String images;

}
