package com.uniclub.repository;

import com.uniclub.entity.Variant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VariantRepository extends JpaRepository<Variant, Integer>{

    List<Variant> findByProductId(Integer productId);

    List<Variant> findByStatus(Byte status);

    List<Variant> findBySizeId(Integer sizeId);

    List<Variant> findByColorId(Integer colorId);
}
