package com.uniclub.controller;

import com.uniclub.dto.request.Supplier.CreateSupplierRequest;
import com.uniclub.dto.request.Supplier.UpdateSupplierRequest;
import com.uniclub.dto.response.Supplier.SupplierResponse;
import com.uniclub.service.SupplierService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/suppliers")
@Validated
@CrossOrigin(origins = "*") // tuỳ bạn, có thể cấu hình CORS global
public class SupplierController {

    @Autowired
    private SupplierService supplierService;

    // CREATE
    @PostMapping
    public ResponseEntity<SupplierResponse> create(@Valid @RequestBody CreateSupplierRequest request) {
        SupplierResponse created = supplierService.createSupplier(request);
        // Trả về 201 + Location header
        return ResponseEntity
                .created(URI.create("/api/suppliers/" + created.getId()))
                .body(created);
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<SupplierResponse> update(@PathVariable Integer id,
                                                @Valid @RequestBody UpdateSupplierRequest request) {
        SupplierResponse updated = supplierService.updateSupplier(id, request);
        return ResponseEntity.ok(updated);
    }

    // GET ALL
    @GetMapping
    public ResponseEntity<List<SupplierResponse>> getAll() {
        return ResponseEntity.ok(supplierService.getAllSuppliers());
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<SupplierResponse> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(supplierService.getSupplierById(id));
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        supplierService.deleteSupplier(id);
        return ResponseEntity.noContent().build(); // 204
    }
}
