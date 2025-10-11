package com.uniclub.service.impl;

import com.uniclub.dto.request.Role.CreateRoleRequest;
import com.uniclub.dto.request.Role.UpdateRoleRequest;
import com.uniclub.dto.response.Role.RoleResponse;
import com.uniclub.entity.Role;
import com.uniclub.exception.ResourceNotFoundException;
import com.uniclub.repository.RoleRepository;
import com.uniclub.service.RoleService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public List<RoleResponse> getAllRoles() {
        return roleRepository.findAll().stream()
                .map(RoleResponse::fromEntity)
                .toList();
    }

    @Override
    public RoleResponse getRoleById(Integer roleId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role", "id", roleId));
        return RoleResponse.fromEntity(role);
    }

    @Override
    public RoleResponse createRole(CreateRoleRequest request) {
        // Kiểm tra trùng tên role
        if (roleRepository.existsByNameIgnoreCase(request.getName())) {
            throw new IllegalArgumentException("Role name already exists");
        }

        Role role = new Role();
        role.setName(request.getName());
        role.setDescription(request.getDescription());

        Role updatedRole = roleRepository.save(role);
        return RoleResponse.fromEntity(updatedRole);
    }

    @Override
    public RoleResponse updateRole(Integer roleId, UpdateRoleRequest request) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role", "id", roleId));

        if (request.getName() != null && !request.getName().equals(role.getName())) {
            // Kiểm tra trùng tên role
            if (roleRepository.existsByNameIgnoreCase(request.getName())) {
                throw new IllegalArgumentException("Role name already exists");
            }
            role.setName(request.getName());
        }

        if (request.getDescription() != null) {
            role.setDescription(request.getDescription());
        }

        if (request.getStatus() != null) {
            role.setStatus(request.getStatus());
        }

        Role updatedRole = roleRepository.save(role);
        return RoleResponse.fromEntity(updatedRole);
    }

    @Override
    public void deleteRole(Integer roleId) {
        if (!roleRepository.existsById(roleId)) {
            throw new ResourceNotFoundException("Role", "id", roleId);
        }
        roleRepository.deleteById(roleId);
    }

    @Override
    public void inactiveRole(Integer roleId) {
        Role existingRole = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role", "id", roleId));

        existingRole.setStatus((byte) 0);
        roleRepository.save(existingRole);
    }
}
