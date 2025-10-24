package com.uniclub.controller;

import com.uniclub.dto.request.Variant.CreateVariantRequest;
import com.uniclub.dto.request.Variant.UpdateVariantRequest;
import com.uniclub.dto.response.Variant.VariantResponse;
import com.uniclub.service.VariantService;
import com.uniclub.service.CloudinaryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/variants")
@Validated
@CrossOrigin(origins = "*")
public class VariantController {

    @Autowired
    private VariantService variantService;

    @Autowired
    private CloudinaryService cloudinaryService;

    // CREATE
    @PostMapping
    public ResponseEntity<VariantResponse> create(@Valid @RequestBody CreateVariantRequest request) {
        VariantResponse created = variantService.createVariant(request);
        return ResponseEntity
                .created(URI.create("/api/variants/" + created.getSku()))
                .body(created);
    }

    // UPDATE
    @PutMapping("/{sku}")
    public ResponseEntity<VariantResponse> update(@PathVariable Integer sku,
                                                @Valid @RequestBody UpdateVariantRequest request) {
        VariantResponse updated = variantService.updateVariant(sku, request);
        return ResponseEntity.ok(updated);
    }

    // GET ALL
    @GetMapping
    public ResponseEntity<List<VariantResponse>> getAll(@RequestParam(required = false) Byte status) {
        return ResponseEntity.ok(variantService.getAllVariants(status));
    }

    // GET BY SKU
    @GetMapping("/{sku}")
    public ResponseEntity<VariantResponse> getBySku(@PathVariable Integer sku) {
        return ResponseEntity.ok(variantService.getBySku(sku));
    }

    // INCREASE STOCK
    @PutMapping("/{sku}/stock/increase")
    public ResponseEntity<VariantResponse> increaseStock(@PathVariable Integer sku,
                                                      @RequestParam Integer amount) {
        VariantResponse updated = variantService.increaseStock(sku, amount);
        return ResponseEntity.ok(updated);
    }

    // DECREASE STOCK
    @PutMapping("/{sku}/stock/decrease")
    public ResponseEntity<VariantResponse> decreaseStock(@PathVariable Integer sku,
                                                      @RequestParam Integer amount) {
        VariantResponse updated = variantService.decreaseStock(sku, amount);
        return ResponseEntity.ok(updated);
    }

    // CHECK COMBINATION EXISTS
    @GetMapping("/check-combination")
    public ResponseEntity<Boolean> checkCombination(@RequestParam Integer productId,
                                                 @RequestParam Integer sizeId,
                                                 @RequestParam Integer colorId) {
        boolean exists = variantService.existsByCombination(productId, sizeId, colorId);
        return ResponseEntity.ok(exists);
    }

    // UPLOAD IMAGE FOR VARIANT
    @PostMapping("/{sku}/upload-image")
    public ResponseEntity<?> uploadVariantImage(@PathVariable Integer sku, 
                                               @RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("File is empty");
            }

            // Validate file type
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                return ResponseEntity.badRequest().body("File must be an image");
            }

            // Validate file size (max 5MB)
            if (file.getSize() > 5 * 1024 * 1024) {
                return ResponseEntity.badRequest().body("File size must be less than 5MB");
            }

            // Upload image to Cloudinary
            String imageUrl = cloudinaryService.uploadImage(file, "uniclub/variants");
            
            // Update variant with new image URL
            VariantResponse updated = variantService.updateVariantImage(sku, imageUrl);
            
            Map<String, Object> response = new HashMap<>();
            response.put("variant", updated);
            response.put("imageUrl", imageUrl);
            response.put("message", "Image uploaded and variant updated successfully");

            return ResponseEntity.ok(response);

        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Failed to upload image: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Unexpected error: " + e.getMessage());
        }
    }

    // DELETE
    @DeleteMapping("/{sku}")
    public ResponseEntity<Void> delete(@PathVariable Integer sku) {
        variantService.deleteVariantBySku(sku);
        return ResponseEntity.noContent().build();
    }
}
