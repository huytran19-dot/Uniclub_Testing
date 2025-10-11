package com.uniclub.service.impl;

import com.uniclub.dto.request.Supplier.CreateSupplierRequest;
import com.uniclub.dto.request.Supplier.UpdateSupplierRequest;
import com.uniclub.dto.response.Supplier.SupplierResponse;
import com.uniclub.entity.Supplier;
import com.uniclub.exception.ResourceNotFoundException;
import com.uniclub.repository.SupplierRepository;
import com.uniclub.service.SupplierService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class SupplierServiceImpl implements SupplierService {
    @Autowired
    private SupplierRepository supplierRepository;

    @Override
    public SupplierResponse createSupplier(CreateSupplierRequest request) {
        // Check if supplier with same name already exists
        if (supplierRepository.existsByNameIgnoreCase(request.getName())) {
            throw new IllegalArgumentException("Nhà cung cấp với tên này đã tồn tại");
        }

        // Check if email already exists (if provided)
        if (request.getEmail() != null && !request.getEmail().trim().isEmpty() 
            && supplierRepository.existsByEmailIgnoreCase(request.getEmail())) {
            throw new IllegalArgumentException("Email này đã được sử dụng bởi nhà cung cấp khác");
        }

        // Check if phone already exists (if provided)
        if (request.getPhone() != null && !request.getPhone().trim().isEmpty() 
            && supplierRepository.existsByPhone(request.getPhone())) {
            throw new IllegalArgumentException("Số điện thoại này đã được sử dụng bởi nhà cung cấp khác");
        }

        Supplier supplier = new Supplier();
        supplier.setName(request.getName());
        supplier.setContactPerson(request.getContactPerson());
        supplier.setPhone(request.getPhone());
        supplier.setEmail(request.getEmail());
        supplier.setAddress(request.getAddress());

        Supplier savedSupplier = supplierRepository.save(supplier);
        return SupplierResponse.fromEntity(savedSupplier);
    }

    @Override
    public SupplierResponse updateSupplier(Integer supplierId, UpdateSupplierRequest request) {
        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier", "id", supplierId));

        // Check if name is being changed and if it already exists
        if (request.getName() != null && !request.getName().equals(supplier.getName())) {
            if (supplierRepository.existsByNameIgnoreCase(request.getName())) {
                throw new IllegalArgumentException("Nhà cung cấp với tên này đã tồn tại");
            }
            supplier.setName(request.getName());
        }

        // Check if email is being changed and if it already exists
        if (request.getEmail() != null && !request.getEmail().equals(supplier.getEmail())) {
            if (supplierRepository.existsByEmailIgnoreCase(request.getEmail())) {
                throw new IllegalArgumentException("Email này đã được sử dụng bởi nhà cung cấp khác");
            }
            supplier.setEmail(request.getEmail());
        }

        // Check if phone is being changed and if it already exists
        if (request.getPhone() != null && !request.getPhone().equals(supplier.getPhone())) {
            if (supplierRepository.existsByPhone(request.getPhone())) {
                throw new IllegalArgumentException("Số điện thoại này đã được sử dụng bởi nhà cung cấp khác");
            }
            supplier.setPhone(request.getPhone());
        }

        if (request.getContactPerson() != null) {
            supplier.setContactPerson(request.getContactPerson());
        }
        if (request.getAddress() != null) {
            supplier.setAddress(request.getAddress());
        }
        if (request.getStatus() != null) {
            supplier.setStatus(request.getStatus());
        }

        Supplier updatedSupplier = supplierRepository.save(supplier);
        return SupplierResponse.fromEntity(updatedSupplier);
    }

    @Override
    public List<SupplierResponse> getAllSuppliers() {
        return supplierRepository.findAll()
                .stream()
                .map(SupplierResponse::fromEntity)
                .toList();
    }

    @Override
    public SupplierResponse getSupplierById(Integer supplierId) {
        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier", "id", supplierId));
        return SupplierResponse.fromEntity(supplier);
    }

    // Hard delete
    @Override
    public void deleteSupplier(Integer supplierId) {
        if (!supplierRepository.existsById(supplierId)) {
            throw new ResourceNotFoundException("Supplier", "id", supplierId);
        }
        supplierRepository.deleteById(supplierId);
    }
}
