package com.uniclub.entity;

import lombok.Data;
import java.io.Serializable;

@Data
public class OrderVariantId implements Serializable {
    private Integer order;
    private Integer variant;
}