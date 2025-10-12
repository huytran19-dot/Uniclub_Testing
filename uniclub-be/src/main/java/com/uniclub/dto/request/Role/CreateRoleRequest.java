package com.uniclub.dto.request.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.Data;

@Data
public class CreateRoleRequest {
    @NotBlank(message = "Role name cannot be empty")
    @Size(max = 50, message = "Role name cannot exceed 50 characters")
    private String name;

    @Size(max = 255, message = "Description cannot exceed 255 characters")
    private String description;
}
