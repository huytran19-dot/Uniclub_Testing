package com.uniclub.dto.response.Role;

import com.uniclub.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleResponse {
    private Integer id;
    private String name;
    private String description;
    private Byte status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static RoleResponse fromEntity(Role role) {
        if (role == null) return null;
        
        return RoleResponse.builder()
                .id(role.getId())
                .name(role.getName())
                .description(role.getDescription())
                .status(role.getStatus())
                .createdAt(role.getCreatedAt())
                .updatedAt(role.getUpdatedAt())
                .build();
    }
}