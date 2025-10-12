package com.uniclub.controller;

import com.uniclub.dto.request.Role.CreateRoleRequest;
import com.uniclub.dto.request.Role.UpdateRoleRequest;
import com.uniclub.dto.response.Role.RoleResponse;
import com.uniclub.service.RoleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    // Lấy toàn bộ role
    @GetMapping
    public ResponseEntity<List<RoleResponse>> getAllRoles() {
        return ResponseEntity.ok(roleService.getAllRoles());
    }

    // Lấy role theo ID
    @GetMapping("/{roleId}")
    public ResponseEntity<RoleResponse> getRoleById(@PathVariable Integer roleId) {
        return ResponseEntity.ok(roleService.getRoleById(roleId));
    }

    // Tạo mới role
    @PostMapping
    public ResponseEntity<RoleResponse> createRole(
            @Valid @RequestBody CreateRoleRequest request) {
        RoleResponse response = roleService.createRole(request);
        return ResponseEntity.ok(response);
    }

    // Cập nhật role
    @PutMapping("/{roleId}")
    public ResponseEntity<RoleResponse> updateRole(
            @PathVariable Integer roleId,
            @Valid @RequestBody UpdateRoleRequest request) {
        RoleResponse response = roleService.updateRole(roleId, request);
        return ResponseEntity.ok(response);
    }

    // Xóa role
    @DeleteMapping("/{roleId}")
    public ResponseEntity<Void> deleteRole(@PathVariable Integer roleId) {
        roleService.deleteRole(roleId);
        return ResponseEntity.noContent().build();
    }

    // Inactivate role (soft delete)
    @PatchMapping("/{roleId}/inactive")
    public ResponseEntity<Void> inactiveRole(@PathVariable Integer roleId) {
        roleService.inactiveRole(roleId);
        return ResponseEntity.noContent().build();
    }
}
