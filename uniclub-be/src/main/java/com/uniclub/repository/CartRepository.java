package com.uniclub.repository;

import com.uniclub.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Integer>{
    Optional<Cart> findByUserId(Integer userId);
    boolean existsByUserId(Integer userId);
}
