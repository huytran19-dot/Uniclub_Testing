package com.uniclub.entity;

import jakarta.persistence.Entity;
import lombok.Data;

@Data
@Entity(name = "order_variant")
public class OrderVariant {

    private int quantity;
    private double price;
}
