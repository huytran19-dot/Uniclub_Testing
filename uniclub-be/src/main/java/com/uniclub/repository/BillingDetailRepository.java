package com.uniclub.repository;

import com.uniclub.entity.BillingDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BillingDetailRepository extends JpaRepository<BillingDetail, Integer> {
    List<BillingDetail> findByEmail(String email);
    List<BillingDetail> findByPhone(String phone);
    boolean existsByEmail(String email);
}
