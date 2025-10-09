package com.uniclub.controller;

import com.uniclub.dto.request.Brand.CreateBrandRequest;
import com.uniclub.dto.request.Brand.UpdateBrandRequest;
import com.uniclub.dto.response.Brand.BrandResponse;
import com.uniclub.service.BrandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/brands")
@Validated
@CrossOrigin(origins = "*") // tuỳ bạn, có thể cấu hình CORS global
public class BrandController {

    @Autowired
    private BrandService brandService;

    // CREATE
    @PostMapping
    public ResponseEntity<BrandResponse> create(@Valid @RequestBody CreateBrandRequest request) {
        BrandResponse created = brandService.createBrand(request);
        // Trả về 201 + Location header
        return ResponseEntity
                .created(URI.create("/api/brands/" + created.getId()))
                .body(created);
    }

    // UPDATE
    @PutMapping
    public ResponseEntity<BrandResponse> update(@PathVariable Integer brandId,
                                                @Valid @RequestBody UpdateBrandRequest request) {
        BrandResponse updated = brandService.updateBrand(brandId, request);
        return ResponseEntity.ok(updated);
    }

    // GET ALL
    @GetMapping
    public ResponseEntity<List<BrandResponse>> getAll() {
        return ResponseEntity.ok(brandService.getAllBrands());
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<BrandResponse> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(brandService.getBrandById(id));
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        brandService.deleteBrand(id);
        return ResponseEntity.noContent().build(); // 204
    }
}

