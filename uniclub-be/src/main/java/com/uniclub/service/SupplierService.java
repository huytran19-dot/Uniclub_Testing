package com.uniclub.service;

import com.uniclub.dto.request.Supplier.CreateSupplierRequest;
import com.uniclub.dto.request.Supplier.UpdateSupplierRequest;
import com.uniclub.dto.response.Supplier.SupplierResponse;

import java.util.List;

public interface SupplierService {
    SupplierResponse createSupplier(CreateSupplierRequest request);
    SupplierResponse updateSupplier(Integer supplierId, UpdateSupplierRequest request);
    void deleteSupplier(Integer id);

    List<SupplierResponse> getAllSuppliers();
    SupplierResponse getSupplierById(Integer id);
}
