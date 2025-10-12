package com.uniclub.controller;

import com.uniclub.dto.request.GrnHeader.CreateGrnHeaderRequest;
import com.uniclub.dto.request.GrnHeader.UpdateGrnHeaderRequest;
import com.uniclub.dto.response.GrnHeader.GrnHeaderResponse;
import com.uniclub.service.GrnHeaderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/grn-headers")
@Validated
@CrossOrigin(origins = "*")
public class GrnHeaderController {

    @Autowired
    private GrnHeaderService grnHeaderService;

    // CREATE
    @PostMapping
    public ResponseEntity<GrnHeaderResponse> create(@Valid @RequestBody CreateGrnHeaderRequest request) {
        GrnHeaderResponse created = grnHeaderService.createGrnHeader(request);
        return ResponseEntity
                .created(URI.create("/api/grn-headers/" + created.getId()))
                .body(created);
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<GrnHeaderResponse> update(@PathVariable Integer id,
                                                @Valid @RequestBody UpdateGrnHeaderRequest request) {
        GrnHeaderResponse updated = grnHeaderService.updateGrnHeader(id, request);
        return ResponseEntity.ok(updated);
    }

    // GET ALL
    @GetMapping
    public ResponseEntity<List<GrnHeaderResponse>> getAll() {
        return ResponseEntity.ok(grnHeaderService.getAllGrnHeaders());
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<GrnHeaderResponse> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(grnHeaderService.getGrnHeaderById(id));
    }

    // GET BY SUPPLIER ID
    @GetMapping("/supplier/{supplierId}")
    public ResponseEntity<List<GrnHeaderResponse>> getBySupplierId(@PathVariable Integer supplierId) {
        return ResponseEntity.ok(grnHeaderService.getGrnHeadersBySupplierId(supplierId));
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        grnHeaderService.deleteGrnHeader(id);
        return ResponseEntity.noContent().build();
    }
}
