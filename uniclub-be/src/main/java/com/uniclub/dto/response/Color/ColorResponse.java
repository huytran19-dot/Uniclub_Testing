package com.uniclub.dto.response.Color;

import com.uniclub.entity.Color;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ColorResponse {

    private Integer id;
    private String name;
    private String hexCode;
    private Byte status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ColorResponse fromEntity(Color color) {
        if (color == null) return null;

        ColorResponse response = new ColorResponse();
        response.setId(color.getId());
        response.setName(color.getName());
        response.setHexCode(color.getHexCode());
        response.setStatus(color.getStatus());
        response.setCreatedAt(color.getCreatedAt());
        response.setUpdatedAt(color.getUpdatedAt());
        return response;
    }
}
