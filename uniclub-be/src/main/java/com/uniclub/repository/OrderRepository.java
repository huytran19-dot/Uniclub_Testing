package com.uniclub.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.uniclub.entity.Order;
import com.uniclub.entity.enums.OrderStatus;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    @Query("SELECT o FROM Order o LEFT JOIN FETCH o.user WHERE o.user.id = :userId ORDER BY o.createdAt DESC")
    List<Order> findByUserId(@Param("userId") Integer userId);
    
    @Query("SELECT DISTINCT o FROM Order o LEFT JOIN FETCH o.orderVariants WHERE o.status = :status")
    List<Order> findByStatus(@Param("status") OrderStatus status);
    
    boolean existsByUserId(Integer userId);
}
