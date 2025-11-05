package com.uniclub.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.uniclub.entity.Cart;

public interface CartRepository extends JpaRepository<Cart, Integer>{
    
    // ✅ Eager load User khi tìm theo userId  
    @Query("SELECT c FROM Cart c LEFT JOIN FETCH c.user WHERE c.user.id = :userId")
    Optional<Cart> findByUserId(@Param("userId") Integer userId);
    
    // ✅ Check existence
    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Cart c WHERE c.user.id = :userId")
    boolean existsByUserId(@Param("userId") Integer userId);
    
    // ✅ Eager load User khi tìm theo cart id
    @Query("SELECT c FROM Cart c LEFT JOIN FETCH c.user WHERE c.id = :id")
    Optional<Cart> findByIdWithUser(@Param("id") Integer id);
    
    // ✅ Get all carts với User được fetch
    @Query("SELECT c FROM Cart c LEFT JOIN FETCH c.user")
    List<Cart> findAllWithUsers();
}
