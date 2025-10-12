package com.uniclub.service;

import com.uniclub.dto.request.BillingDetail.CreateBillingDetailRequest;
import com.uniclub.dto.request.BillingDetail.UpdateBillingDetailRequest;
import com.uniclub.dto.response.BillingDetail.BillingDetailResponse;

import java.util.List;

public interface BillingDetailService {
    BillingDetailResponse createBillingDetail(CreateBillingDetailRequest request);
    BillingDetailResponse updateBillingDetail(Integer billingDetailId, UpdateBillingDetailRequest request);
    void deleteBillingDetail(Integer id);

    List<BillingDetailResponse> getAllBillingDetails();
    BillingDetailResponse getBillingDetailById(Integer id);
    List<BillingDetailResponse> getBillingDetailsByEmail(String email);
}
