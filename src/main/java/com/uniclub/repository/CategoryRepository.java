package com.uniclub.repository;

import com.uniclub.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

    boolean existsByNameIgnoreCase(String name);
    List<Category> findByNameContainingIgnoreCase(String keyword);
}
