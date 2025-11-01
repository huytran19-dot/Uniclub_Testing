package com.uniclub.dto.response.User;

import com.uniclub.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Integer id;
    private String email;
    private String fullname;
    private Integer roleId;
    private String roleName;
    private Byte status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static UserResponse fromEntity(User user) {
        if (user == null) return null;
        
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .fullname(user.getFullname())
                .roleId(user.getRole() != null ? user.getRole().getId() : null)
                .roleName(user.getRole() != null ? user.getRole().getName() : null)
                .status(user.getStatus())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}