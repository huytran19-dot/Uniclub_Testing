package com.uniclub.repository;

import com.uniclub.entity.BillingDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BillingDetailRepository extends JpaRepository<BillingDetail, Integer> {
    List<BillingDetail> findByEmail(String email);
    List<BillingDetail> findByPhone(String phone);
    boolean existsByEmail(String email);
    Optional<BillingDetail> findByOrderId(Integer orderId);
    List<BillingDetail> findAllByOrderId(Integer orderId);
}
