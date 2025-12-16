package com.uniclub.repository;

import com.uniclub.entity.Order;
import com.uniclub.entity.OrderVariant;
import com.uniclub.entity.OrderVariantId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderVariantRepository extends JpaRepository<OrderVariant, OrderVariantId> {
    List<OrderVariant> findByOrder(Order order);
    
    @Query("SELECT ov FROM OrderVariant ov WHERE ov.order.id = :orderId")
    List<OrderVariant> findByOrderId(@Param("orderId") Integer orderId);
}
