package com.uniclub.controller;

import com.uniclub.dto.request.Color.CreateColorRequest;
import com.uniclub.dto.request.Color.UpdateColorRequest;
import com.uniclub.dto.response.Color.ColorResponse;
import com.uniclub.service.ColorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/colors")
@Validated
@CrossOrigin(origins = "*")
public class ColorController {

    @Autowired
    private ColorService colorService;

    // CREATE
    @PostMapping
    public ResponseEntity<ColorResponse> create(@Valid @RequestBody CreateColorRequest request) {
        ColorResponse created = colorService.createColor(request);
        return ResponseEntity
                .created(URI.create("/api/colors/" + created.getId()))
                .body(created);
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<ColorResponse> update(@PathVariable Integer id,
                                                @Valid @RequestBody UpdateColorRequest request) {
        ColorResponse updated = colorService.updateColor(id, request);
        return ResponseEntity.ok(updated);
    }

    // GET ALL
    @GetMapping
    public ResponseEntity<List<ColorResponse>> getAll() {
        return ResponseEntity.ok(colorService.getAllColors());
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<ColorResponse> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(colorService.getColorById(id));
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        colorService.deleteColor(id);
        return ResponseEntity.noContent().build();
    }
}
