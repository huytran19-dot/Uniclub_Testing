package com.uniclub.controller;

import com.uniclub.dto.request.Size.CreateSizeRequest;
import com.uniclub.dto.request.Size.UpdateSizeRequest;
import com.uniclub.dto.response.Size.SizeResponse;
import com.uniclub.service.SizeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/sizes")
@Validated
@CrossOrigin(origins = "*")
public class SizeController {

    @Autowired
    private SizeService sizeService;

    // CREATE
    @PostMapping
    public ResponseEntity<SizeResponse> create(@Valid @RequestBody CreateSizeRequest request) {
        SizeResponse created = sizeService.createSize(request);
        return ResponseEntity
                .created(URI.create("/api/sizes/" + created.getId()))
                .body(created);
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<SizeResponse> update(@PathVariable Integer id,
                                                @Valid @RequestBody UpdateSizeRequest request) {
        SizeResponse updated = sizeService.updateSize(id, request);
        return ResponseEntity.ok(updated);
    }

    // GET ALL
    @GetMapping
    public ResponseEntity<List<SizeResponse>> getAll() {
        return ResponseEntity.ok(sizeService.getAllSizes());
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<SizeResponse> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(sizeService.getSizeById(id));
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        sizeService.deleteSize(id);
        return ResponseEntity.noContent().build();
    }
}
