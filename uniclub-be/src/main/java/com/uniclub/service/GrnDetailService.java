package com.uniclub.service;

import com.uniclub.dto.request.GrnDetail.CreateGrnDetailRequest;
import com.uniclub.dto.request.GrnDetail.UpdateGrnDetailRequest;
import com.uniclub.dto.response.GrnDetail.GrnDetailResponse;

import java.util.List;

public interface GrnDetailService {
    GrnDetailResponse createGrnDetail(CreateGrnDetailRequest request);
    GrnDetailResponse updateGrnDetail(Integer grnDetailId, UpdateGrnDetailRequest request);
    void deleteGrnDetail(Integer id);

    List<GrnDetailResponse> getAllGrnDetails();
    GrnDetailResponse getGrnDetailById(Integer id);
    List<GrnDetailResponse> getGrnDetailsByGrnHeaderId(Integer grnHeaderId);
}
