package com.uniclub.repository;

import com.uniclub.entity.OrderVariant;
import com.uniclub.entity.OrderVariantId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderVariantRepository extends JpaRepository<OrderVariant, OrderVariantId> {
}
