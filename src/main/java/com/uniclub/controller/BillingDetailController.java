package com.uniclub.controller;

import com.uniclub.dto.request.BillingDetail.CreateBillingDetailRequest;
import com.uniclub.dto.request.BillingDetail.UpdateBillingDetailRequest;
import com.uniclub.dto.response.BillingDetail.BillingDetailResponse;
import com.uniclub.service.BillingDetailService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/billing-details")
@Validated
@CrossOrigin(origins = "*")
public class BillingDetailController {

    @Autowired
    private BillingDetailService billingDetailService;

    // CREATE
    @PostMapping
    public ResponseEntity<BillingDetailResponse> create(@Valid @RequestBody CreateBillingDetailRequest request) {
        BillingDetailResponse created = billingDetailService.createBillingDetail(request);
        return ResponseEntity
                .created(URI.create("/api/billing-details/" + created.getId()))
                .body(created);
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<BillingDetailResponse> update(@PathVariable Integer id,
                                                @Valid @RequestBody UpdateBillingDetailRequest request) {
        BillingDetailResponse updated = billingDetailService.updateBillingDetail(id, request);
        return ResponseEntity.ok(updated);
    }

    // GET ALL
    @GetMapping
    public ResponseEntity<List<BillingDetailResponse>> getAll() {
        return ResponseEntity.ok(billingDetailService.getAllBillingDetails());
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<BillingDetailResponse> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(billingDetailService.getBillingDetailById(id));
    }

    // GET BY EMAIL
    @GetMapping("/email/{email}")
    public ResponseEntity<List<BillingDetailResponse>> getByEmail(@PathVariable String email) {
        return ResponseEntity.ok(billingDetailService.getBillingDetailsByEmail(email));
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        billingDetailService.deleteBillingDetail(id);
        return ResponseEntity.noContent().build();
    }
}
