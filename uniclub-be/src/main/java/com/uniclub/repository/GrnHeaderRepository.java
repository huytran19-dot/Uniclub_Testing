package com.uniclub.repository;

import com.uniclub.entity.GrnHeader;
import com.uniclub.entity.enums.GrnStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GrnHeaderRepository extends JpaRepository<GrnHeader, Integer> {
    List<GrnHeader> findBySupplierId(Integer supplierId);
    List<GrnHeader> findByStatus(GrnStatus status);
    boolean existsBySupplierId(Integer supplierId);
}
