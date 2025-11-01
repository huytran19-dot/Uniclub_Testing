package com.uniclub.entity;

import java.io.Serializable;

import lombok.Data;

@Data
public class OrderVariantId implements Serializable {
    private Integer order;
    private Integer variant;
}