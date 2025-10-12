package com.uniclub.dto.response.User;

import com.uniclub.dto.response.Role.RoleResponse;
import com.uniclub.entity.User;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserResponse {
    private Integer id;
    private String email;
    private String fullname;
    private RoleResponse role;
    private Byte status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static UserResponse fromEntity(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setEmail(user.getEmail());
        response.setFullname(user.getFullname());
        if (user.getRole() != null) {
            response.setRole(RoleResponse.fromEntity(user.getRole()));
        }
        response.setStatus(user.getStatus());
        response.setCreatedAt(user.getCreatedAt());
        response.setUpdatedAt(user.getUpdatedAt());

        return response;
    }
}
