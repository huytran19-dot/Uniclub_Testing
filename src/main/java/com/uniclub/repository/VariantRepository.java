package com.uniclub.repository;

import com.uniclub.entity.Variant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VariantRepository extends JpaRepository<Variant, Integer>{

    List<Variant> findByProduct_Id(Integer productId);

    List<Variant> findByStatus(Byte status);

    List<Variant> findBySize_Id(Integer sizeId);

    List<Variant> findByColor_Id(Integer colorId);
}
