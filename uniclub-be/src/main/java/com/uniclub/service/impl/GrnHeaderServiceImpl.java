package com.uniclub.service.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uniclub.dto.request.GrnHeader.CreateGrnHeaderRequest;
import com.uniclub.dto.request.GrnHeader.UpdateGrnHeaderRequest;
import com.uniclub.dto.response.GrnHeader.GrnHeaderResponse;
import com.uniclub.entity.GrnDetail;
import com.uniclub.entity.GrnHeader;
import com.uniclub.entity.Supplier;
import com.uniclub.entity.Variant;
import com.uniclub.entity.enums.GrnStatus;
import com.uniclub.exception.ResourceNotFoundException;
import com.uniclub.repository.GrnDetailRepository;
import com.uniclub.repository.GrnHeaderRepository;
import com.uniclub.repository.SupplierRepository;
import com.uniclub.repository.VariantRepository;
import com.uniclub.service.GrnHeaderService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class GrnHeaderServiceImpl implements GrnHeaderService {
    @Autowired
    private GrnHeaderRepository grnHeaderRepository;
    
    @Autowired
    private GrnDetailRepository grnDetailRepository;
    
    @Autowired
    private VariantRepository variantRepository;
    
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
        // Use provided date or default to current date
        grnHeader.setReceivedDate(request.getReceivedDate() != null ? request.getReceivedDate() : LocalDate.now());
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
            GrnStatus oldStatus = grnHeader.getStatus();
            grnHeader.setStatus(request.getStatus());
            
            System.out.println("Status change: " + oldStatus + " -> " + request.getStatus());
            
            // If status is being changed to COMPLETED, update inventory
            if (request.getStatus() == GrnStatus.COMPLETED && oldStatus != GrnStatus.COMPLETED) {
                System.out.println("Triggering inventory update...");
                updateInventory(grnHeaderId);
            }
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

    private void updateInventory(Integer grnHeaderId) {
        System.out.println("Updating inventory for GRN Header ID: " + grnHeaderId);
        
        // Get all GRN details for this header
        List<GrnDetail> details = grnDetailRepository.findByGrnHeaderId(grnHeaderId);
        System.out.println("Found " + details.size() + " GRN details");
        
        for (GrnDetail detail : details) {
            System.out.println("Processing detail: " + detail.getId() + ", Quantity: " + detail.getQuantity());
            
            // Update variant quantity
            Variant variant = detail.getVariant();
            if (variant != null) {
                int oldQuantity = variant.getQuantity();
                int newQuantity = oldQuantity + detail.getQuantity();
                System.out.println("Variant SKU: " + variant.getSku() + ", Old quantity: " + oldQuantity + ", Adding: " + detail.getQuantity() + ", New quantity: " + newQuantity);
                
                variant.setQuantity(newQuantity);
                variantRepository.save(variant);
                System.out.println("Updated variant quantity successfully");
            } else {
                System.out.println("Variant is null for detail: " + detail.getId());
            }
        }
        System.out.println("Inventory update completed");
    }
}
