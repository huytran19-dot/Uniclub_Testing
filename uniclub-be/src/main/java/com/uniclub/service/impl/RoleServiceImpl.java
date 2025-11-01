package com.uniclub.service.impl;

import com.uniclub.dto.response.Role.RoleResponse;
import com.uniclub.entity.Role;
import com.uniclub.exception.ResourceNotFoundException;
import com.uniclub.repository.RoleRepository;
import com.uniclub.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public List<RoleResponse> getAllRoles() {
        return roleRepository.findAll()
                .stream()
                .map(RoleResponse::fromEntity)
                .toList();
    }

    @Override
    public RoleResponse getRoleById(Integer roleId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role", "id", roleId));
        return RoleResponse.fromEntity(role);
    }
}