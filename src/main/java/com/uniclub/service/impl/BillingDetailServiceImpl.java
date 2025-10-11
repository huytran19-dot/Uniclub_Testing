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
        billingDetail.setFirstName(request.getFirstName());
        billingDetail.setLastName(request.getLastName());
        billingDetail.setCompanyName(request.getCompanyName());
        billingDetail.setAddress(request.getAddress());
        billingDetail.setTown(request.getTown());
        billingDetail.setState(request.getState());
        billingDetail.setZipCode(request.getZipCode());
        billingDetail.setPhone(request.getPhone());
        billingDetail.setEmail(request.getEmail());

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

        if (request.getFirstName() != null) {
            billingDetail.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null) {
            billingDetail.setLastName(request.getLastName());
        }
        if (request.getCompanyName() != null) {
            billingDetail.setCompanyName(request.getCompanyName());
        }
        if (request.getAddress() != null) {
            billingDetail.setAddress(request.getAddress());
        }
        if (request.getTown() != null) {
            billingDetail.setTown(request.getTown());
        }
        if (request.getState() != null) {
            billingDetail.setState(request.getState());
        }
        if (request.getZipCode() != null) {
            billingDetail.setZipCode(request.getZipCode());
        }
        if (request.getPhone() != null) {
            billingDetail.setPhone(request.getPhone());
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
