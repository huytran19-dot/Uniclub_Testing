package com.uniclub.controller;

import com.uniclub.dto.request.GrnDetail.CreateGrnDetailRequest;
import com.uniclub.dto.request.GrnDetail.UpdateGrnDetailRequest;
import com.uniclub.dto.response.GrnDetail.GrnDetailResponse;
import com.uniclub.service.GrnDetailService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/grn-details")
@Validated
@CrossOrigin(origins = "*")
public class GrnDetailController {

    @Autowired
    private GrnDetailService grnDetailService;

    // CREATE
    @PostMapping
    public ResponseEntity<GrnDetailResponse> create(@Valid @RequestBody CreateGrnDetailRequest request) {
        GrnDetailResponse created = grnDetailService.createGrnDetail(request);
        return ResponseEntity
                .created(URI.create("/api/grn-details/" + created.getId()))
                .body(created);
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<GrnDetailResponse> update(@PathVariable Integer id,
                                                @Valid @RequestBody UpdateGrnDetailRequest request) {
        GrnDetailResponse updated = grnDetailService.updateGrnDetail(id, request);
        return ResponseEntity.ok(updated);
    }

    // GET ALL
    @GetMapping
    public ResponseEntity<List<GrnDetailResponse>> getAll() {
        return ResponseEntity.ok(grnDetailService.getAllGrnDetails());
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<GrnDetailResponse> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(grnDetailService.getGrnDetailById(id));
    }

    // GET BY GRN HEADER ID
    @GetMapping("/grn-header/{grnHeaderId}")
    public ResponseEntity<List<GrnDetailResponse>> getByGrnHeaderId(@PathVariable Integer grnHeaderId) {
        return ResponseEntity.ok(grnDetailService.getGrnDetailsByGrnHeaderId(grnHeaderId));
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        grnDetailService.deleteGrnDetail(id);
        return ResponseEntity.noContent().build();
    }
}
