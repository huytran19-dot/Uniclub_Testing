package com.uniclub.repository;

import com.uniclub.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Integer>{
    List<CartItem> findByCartId(Integer cartId);
    Optional<CartItem> findByCartIdAndVariantSku(Integer cartId, Integer variantSku);
    boolean existsByCartIdAndVariantSku(Integer cartId, Integer variantSku);
}
