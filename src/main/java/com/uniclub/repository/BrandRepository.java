package com.uniclub.repository;

import com.uniclub.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandRepository extends JpaRepository<Brand, Integer>{
    boolean existsByNameIgnoreCase(String name);
}
