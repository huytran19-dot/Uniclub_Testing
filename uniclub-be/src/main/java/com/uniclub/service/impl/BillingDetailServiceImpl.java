package com.uniclub.service.impl;

import com.uniclub.dto.request.BillingDetail.CreateBillingDetailRequest;
import com.uniclub.dto.request.BillingDetail.UpdateBillingDetailRequest;
import com.uniclub.dto.response.BillingDetail.BillingDetailResponse;
import com.uniclub.entity.BillingDetail;
import com.uniclub.exception.ResourceNotFoundException;
import com.uniclub.repository.BillingDetailRepository;
import com.uniclub.service.BillingDetailService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class BillingDetailServiceImpl implements BillingDetailService {
    @Autowired
    private BillingDetailRepository billingDetailRepository;

    @Override
    public BillingDetailResponse createBillingDetail(CreateBillingDetailRequest request) {
        // Check if email already exists
        if (request.getEmail() != null && !request.getEmail().trim().isEmpty() 
            && billingDetailRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email này đã được sử dụng");
        }

        BillingDetail billingDetail = new BillingDetail();
        billingDetail.setFullName(request.getFullName());
        billingDetail.setPhone(request.getPhone());
        billingDetail.setEmail(request.getEmail());
        billingDetail.setAddress(request.getAddress());
        billingDetail.setProvince(request.getProvince());
        billingDetail.setDistrict(request.getDistrict());
        billingDetail.setWard(request.getWard());
        billingDetail.setNote(request.getNote());

        BillingDetail savedBillingDetail = billingDetailRepository.save(billingDetail);
        return BillingDetailResponse.fromEntity(savedBillingDetail);
    }

    @Override
    public BillingDetailResponse updateBillingDetail(Integer billingDetailId, UpdateBillingDetailRequest request) {
        BillingDetail billingDetail = billingDetailRepository.findById(billingDetailId)
                .orElseThrow(() -> new ResourceNotFoundException("BillingDetail", "id", billingDetailId));

        // Check if email is being changed and if it already exists
        if (request.getEmail() != null && !request.getEmail().equals(billingDetail.getEmail())) {
            if (billingDetailRepository.existsByEmail(request.getEmail())) {
                throw new IllegalArgumentException("Email này đã được sử dụng");
            }
            billingDetail.setEmail(request.getEmail());
        }

        if (request.getFullName() != null) {
            billingDetail.setFullName(request.getFullName());
        }
        if (request.getPhone() != null) {
            billingDetail.setPhone(request.getPhone());
        }
        if (request.getAddress() != null) {
            billingDetail.setAddress(request.getAddress());
        }
        if (request.getProvince() != null) {
            billingDetail.setProvince(request.getProvince());
        }
        if (request.getDistrict() != null) {
            billingDetail.setDistrict(request.getDistrict());
        }
        if (request.getWard() != null) {
            billingDetail.setWard(request.getWard());
        }
        if (request.getNote() != null) {
            billingDetail.setNote(request.getNote());
        }
        if (request.getStatus() != null) {
            billingDetail.setStatus(request.getStatus());
        }

        BillingDetail updatedBillingDetail = billingDetailRepository.save(billingDetail);
        return BillingDetailResponse.fromEntity(updatedBillingDetail);
    }

    @Override
    public List<BillingDetailResponse> getAllBillingDetails() {
        return billingDetailRepository.findAll()
                .stream()
                .map(BillingDetailResponse::fromEntity)
                .toList();
    }

    @Override
    public BillingDetailResponse getBillingDetailById(Integer billingDetailId) {
        BillingDetail billingDetail = billingDetailRepository.findById(billingDetailId)
                .orElseThrow(() -> new ResourceNotFoundException("BillingDetail", "id", billingDetailId));
        return BillingDetailResponse.fromEntity(billingDetail);
    }

    @Override
    public List<BillingDetailResponse> getBillingDetailsByEmail(String email) {
        return billingDetailRepository.findByEmail(email)
                .stream()
                .map(BillingDetailResponse::fromEntity)
                .toList();
    }

    @Override
    public void deleteBillingDetail(Integer billingDetailId) {
        if (!billingDetailRepository.existsById(billingDetailId)) {
            throw new ResourceNotFoundException("BillingDetail", "id", billingDetailId);
        }
        billingDetailRepository.deleteById(billingDetailId);
    }
}
