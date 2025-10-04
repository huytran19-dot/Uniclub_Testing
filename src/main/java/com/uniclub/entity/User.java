package com.uniclub.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String email;
    private String password;

    @Column(name = "full_name")
    private String fullname;
}
