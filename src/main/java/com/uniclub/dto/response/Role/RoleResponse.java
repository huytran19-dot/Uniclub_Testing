package com.uniclub.dto.response.Role;

import com.uniclub.entity.Role;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RoleResponse {
    private Integer id;
    private String name;
    private String description;
    private Byte status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static RoleResponse fromEntity(Role role) {
        RoleResponse response = new RoleResponse();
        response.setId(role.getId());
        response.setName(role.getName());
        response.setDescription(role.getDescription());
        response.setStatus(role.getStatus());
        response.setCreatedAt(role.getCreatedAt());
        response.setUpdatedAt(role.getUpdatedAt());

        return response;
    }
}
