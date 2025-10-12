package com.uniclub.service;

import com.uniclub.dto.request.GrnHeader.CreateGrnHeaderRequest;
import com.uniclub.dto.request.GrnHeader.UpdateGrnHeaderRequest;
import com.uniclub.dto.response.GrnHeader.GrnHeaderResponse;

import java.util.List;

public interface GrnHeaderService {
    GrnHeaderResponse createGrnHeader(CreateGrnHeaderRequest request);
    GrnHeaderResponse updateGrnHeader(Integer grnHeaderId, UpdateGrnHeaderRequest request);
    void deleteGrnHeader(Integer id);

    List<GrnHeaderResponse> getAllGrnHeaders();
    GrnHeaderResponse getGrnHeaderById(Integer id);
    List<GrnHeaderResponse> getGrnHeadersBySupplierId(Integer supplierId);
}