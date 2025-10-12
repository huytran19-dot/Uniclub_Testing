package com.uniclub.dto.response.Size;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.uniclub.entity.Size;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class SizeResponse {
    private Integer id;
    private String name;
    private Integer status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    public static SizeResponse fromEntity(Size size) {
        if (size == null) return null;
        SizeResponse res = new SizeResponse();
        res.setId(size.getId());
        res.setName(size.getName());
        res.setStatus(size.getStatus());
        res.setCreatedAt(size.getCreatedAt());
        res.setUpdatedAt(size.getUpdatedAt());
        return res;
    }



}
