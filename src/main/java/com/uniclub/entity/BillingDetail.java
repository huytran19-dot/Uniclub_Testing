package com.uniclub.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Data
@Entity(name = "billing_details")
public class BillingDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "company_name")
    private String companyName;

    private String address;
    private String town;
    private String state;

    @Column(name = "zip_code")
    private int zipCode;

    private String phone;
    private String email;

    @CreationTimestamp
    @Column(name="create_date")
    private Date createDate;
}
