package com.uniclub.repository;

import com.uniclub.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    List<Product> findByNameContainingIgnoreCase(String name);
    List<Product> findByDescriptionContainingIgnoreCase(String description);
    List<Product> findByBrand_Id(Integer brandId);
    List<Product> findByCategory_Id(Integer categoryId);

    boolean existsByNameIgnoreCase(String name);
}
