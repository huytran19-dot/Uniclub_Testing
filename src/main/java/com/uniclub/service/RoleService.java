package com.uniclub.service;

import com.uniclub.dto.request.Role.CreateRoleRequest;
import com.uniclub.dto.request.Role.UpdateRoleRequest;
import com.uniclub.dto.response.Role.RoleResponse;

import java.util.List;

public interface RoleService {
    List<RoleResponse> getAllRoles();
    RoleResponse getRoleById(Integer id);

    RoleResponse createRole(CreateRoleRequest request);
    RoleResponse updateRole(Integer id, UpdateRoleRequest request);
    void deleteRole(Integer id);
    void inactiveRole(Integer id);
}
