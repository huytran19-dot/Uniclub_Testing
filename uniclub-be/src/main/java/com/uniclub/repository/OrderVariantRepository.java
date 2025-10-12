package com.uniclub.repository;

import com.uniclub.entity.Order;
import com.uniclub.entity.OrderVariant;
import com.uniclub.entity.OrderVariantId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderVariantRepository extends JpaRepository<OrderVariant, OrderVariantId> {
    List<OrderVariant> findByOrder(Order order);
}
