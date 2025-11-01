package com.uniclub.service;

import com.uniclub.dto.response.Role.RoleResponse;

import java.util.List;

public interface RoleService {
    List<RoleResponse> getAllRoles();
    RoleResponse getRoleById(Integer roleId);
}