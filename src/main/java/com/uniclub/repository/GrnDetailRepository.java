package com.uniclub.repository;

import com.uniclub.entity.GrnDetail;
import com.uniclub.entity.GrnHeader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GrnDetailRepository extends JpaRepository<GrnDetail, Integer> {
    List<GrnDetail> findByGrnHeader(GrnHeader grnHeader);
    List<GrnDetail> findByGrnHeaderId(Integer grnHeaderId);
    boolean existsByGrnHeaderIdAndVariantSku(Integer grnHeaderId, Integer variantSku);
}
