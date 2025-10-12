package com.uniclub.repository;

import com.uniclub.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplierRepository extends JpaRepository<Supplier, Integer>{
    boolean existsByNameIgnoreCase(String name);
    boolean existsByEmailIgnoreCase(String email);
    boolean existsByPhone(String phone);
}
