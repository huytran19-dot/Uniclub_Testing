package com.uniclub.service.impl;

import com.uniclub.dto.request.GrnDetail.CreateGrnDetailRequest;
import com.uniclub.dto.request.GrnDetail.UpdateGrnDetailRequest;
import com.uniclub.dto.response.GrnDetail.GrnDetailResponse;
import com.uniclub.entity.GrnDetail;
import com.uniclub.entity.GrnHeader;
import com.uniclub.entity.Variant;
import com.uniclub.exception.ResourceNotFoundException;
import com.uniclub.repository.GrnDetailRepository;
import com.uniclub.repository.GrnHeaderRepository;
import com.uniclub.repository.VariantRepository;
import com.uniclub.service.GrnDetailService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class GrnDetailServiceImpl implements GrnDetailService {
    @Autowired
    private GrnDetailRepository grnDetailRepository;
    
    @Autowired
    private GrnHeaderRepository grnHeaderRepository;
    
    @Autowired
    private VariantRepository variantRepository;

    @Override
    public GrnDetailResponse createGrnDetail(CreateGrnDetailRequest request) {
        // Check if GRN header exists
        GrnHeader grnHeader = grnHeaderRepository.findById(request.getGrnHeaderId())
                .orElseThrow(() -> new ResourceNotFoundException("GrnHeader", "id", request.getGrnHeaderId()));

        // Check if variant exists
        Variant variant = variantRepository.findById(request.getVariantSku())
                .orElseThrow(() -> new ResourceNotFoundException("Variant", "sku", request.getVariantSku()));

        // Check if GRN detail already exists for this variant
        if (grnDetailRepository.existsByGrnHeaderIdAndVariantSku(request.getGrnHeaderId(), request.getVariantSku())) {
            throw new IllegalArgumentException("Sản phẩm này đã có trong phiếu nhập");
        }

        GrnDetail grnDetail = new GrnDetail();
        grnDetail.setGrnHeader(grnHeader);
        grnDetail.setVariant(variant);
        grnDetail.setQuantity(request.getQuantity());
        grnDetail.setUnitCost(request.getUnitCost());

        GrnDetail savedGrnDetail = grnDetailRepository.save(grnDetail);
        
        // Update GRN header total cost
        updateGrnHeaderTotalCost(grnHeader);
        
        return GrnDetailResponse.fromEntity(savedGrnDetail);
    }

    @Override
    public GrnDetailResponse updateGrnDetail(Integer grnDetailId, UpdateGrnDetailRequest request) {
        GrnDetail grnDetail = grnDetailRepository.findById(grnDetailId)
                .orElseThrow(() -> new ResourceNotFoundException("GrnDetail", "id", grnDetailId));

        if (request.getQuantity() != null) {
            grnDetail.setQuantity(request.getQuantity());
        }
        if (request.getUnitCost() != null) {
            grnDetail.setUnitCost(request.getUnitCost());
        }

        GrnDetail updatedGrnDetail = grnDetailRepository.save(grnDetail);
        
        // Update GRN header total cost
        updateGrnHeaderTotalCost(grnDetail.getGrnHeader());
        
        return GrnDetailResponse.fromEntity(updatedGrnDetail);
    }

    @Override
    public List<GrnDetailResponse> getAllGrnDetails() {
        return grnDetailRepository.findAll()
                .stream()
                .map(GrnDetailResponse::fromEntity)
                .toList();
    }

    @Override
    public GrnDetailResponse getGrnDetailById(Integer grnDetailId) {
        GrnDetail grnDetail = grnDetailRepository.findById(grnDetailId)
                .orElseThrow(() -> new ResourceNotFoundException("GrnDetail", "id", grnDetailId));
        return GrnDetailResponse.fromEntity(grnDetail);
    }

    @Override
    public List<GrnDetailResponse> getGrnDetailsByGrnHeaderId(Integer grnHeaderId) {
        return grnDetailRepository.findByGrnHeaderId(grnHeaderId)
                .stream()
                .map(GrnDetailResponse::fromEntity)
                .toList();
    }

    @Override
    public void deleteGrnDetail(Integer grnDetailId) {
        GrnDetail grnDetail = grnDetailRepository.findById(grnDetailId)
                .orElseThrow(() -> new ResourceNotFoundException("GrnDetail", "id", grnDetailId));
        
        GrnHeader grnHeader = grnDetail.getGrnHeader();
        grnDetailRepository.deleteById(grnDetailId);
        
        // Update GRN header total cost
        updateGrnHeaderTotalCost(grnHeader);
    }

    private void updateGrnHeaderTotalCost(GrnHeader grnHeader) {
        List<GrnDetail> details = grnDetailRepository.findByGrnHeader(grnHeader);
        int totalCost = details.stream()
                .mapToInt(detail -> detail.getQuantity() * detail.getUnitCost())
                .sum();
        grnHeader.setTotalCost(totalCost);
        grnHeaderRepository.save(grnHeader);
    }
}
