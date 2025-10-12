package com.uniclub.dto.response.Category;

import com.uniclub.entity.Category;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CategoryResponse {

    private Integer id;
    private String name;
    private Byte status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static CategoryResponse fromEntity(Category category) {
        if (category == null) return null;

        CategoryResponse response = new CategoryResponse();
        response.setId(category.getId());
        response.setName(category.getName());
        response.setStatus(category.getStatus());
        response.setCreatedAt(category.getCreatedAt());
        response.setUpdatedAt(category.getUpdatedAt());
        return response;
    }
}
