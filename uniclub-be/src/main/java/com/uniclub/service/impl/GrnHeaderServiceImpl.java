package com.uniclub.service.impl;

import com.uniclub.dto.request.GrnHeader.CreateGrnHeaderRequest;
import com.uniclub.dto.request.GrnHeader.UpdateGrnHeaderRequest;
import com.uniclub.dto.response.GrnHeader.GrnHeaderResponse;
import com.uniclub.entity.GrnHeader;
import com.uniclub.entity.Supplier;
import com.uniclub.exception.ResourceNotFoundException;
import com.uniclub.repository.GrnHeaderRepository;
import com.uniclub.repository.SupplierRepository;
import com.uniclub.service.GrnHeaderService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class GrnHeaderServiceImpl implements GrnHeaderService {
    @Autowired
    private GrnHeaderRepository grnHeaderRepository;
    
    @Autowired
    private SupplierRepository supplierRepository;

    @Override
    public GrnHeaderResponse createGrnHeader(CreateGrnHeaderRequest request) {
        // Check if supplier exists
        Supplier supplier = supplierRepository.findById(request.getSupplierId())
                .orElseThrow(() -> new ResourceNotFoundException("Supplier", "id", request.getSupplierId()));

        GrnHeader grnHeader = new GrnHeader();
        grnHeader.setSupplier(supplier);
        grnHeader.setNote(request.getNote());
        grnHeader.setReceivedDate(request.getReceivedDate());
        grnHeader.setTotalCost(0); // Will be calculated when details are added

        GrnHeader savedGrnHeader = grnHeaderRepository.save(grnHeader);
        return GrnHeaderResponse.fromEntity(savedGrnHeader);
    }

    @Override
    public GrnHeaderResponse updateGrnHeader(Integer grnHeaderId, UpdateGrnHeaderRequest request) {
        GrnHeader grnHeader = grnHeaderRepository.findById(grnHeaderId)
                .orElseThrow(() -> new ResourceNotFoundException("GrnHeader", "id", grnHeaderId));

        if (request.getNote() != null) {
            grnHeader.setNote(request.getNote());
        }
        if (request.getReceivedDate() != null) {
            grnHeader.setReceivedDate(request.getReceivedDate());
        }
        if (request.getStatus() != null) {
            grnHeader.setStatus(request.getStatus());
        }

        GrnHeader updatedGrnHeader = grnHeaderRepository.save(grnHeader);
        return GrnHeaderResponse.fromEntity(updatedGrnHeader);
    }

    @Override
    public List<GrnHeaderResponse> getAllGrnHeaders() {
        return grnHeaderRepository.findAll()
                .stream()
                .map(GrnHeaderResponse::fromEntity)
                .toList();
    }

    @Override
    public GrnHeaderResponse getGrnHeaderById(Integer grnHeaderId) {
        GrnHeader grnHeader = grnHeaderRepository.findById(grnHeaderId)
                .orElseThrow(() -> new ResourceNotFoundException("GrnHeader", "id", grnHeaderId));
        return GrnHeaderResponse.fromEntity(grnHeader);
    }

    @Override
    public List<GrnHeaderResponse> getGrnHeadersBySupplierId(Integer supplierId) {
        return grnHeaderRepository.findBySupplierId(supplierId)
                .stream()
                .map(GrnHeaderResponse::fromEntity)
                .toList();
    }

    @Override
    public void deleteGrnHeader(Integer grnHeaderId) {
        if (!grnHeaderRepository.existsById(grnHeaderId)) {
            throw new ResourceNotFoundException("GrnHeader", "id", grnHeaderId);
        }
        grnHeaderRepository.deleteById(grnHeaderId);
    }
}
