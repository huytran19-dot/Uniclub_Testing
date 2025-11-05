package com.uniclub.dto.response.User;

import java.time.LocalDateTime;

import com.uniclub.entity.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Integer id;
    private String email;
    private String fullname;
    private String phone;
    private String address;
    private String provinceCode;
    private String provinceName;
    private String districtCode;
    private String districtName;
    private String wardCode;
    private String wardName;
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
                .phone(user.getPhone())
                .address(user.getAddress())
                .provinceCode(user.getProvinceCode())
                .provinceName(user.getProvinceName())
                .districtCode(user.getDistrictCode())
                .districtName(user.getDistrictName())
                .wardCode(user.getWardCode())
                .wardName(user.getWardName())
                .roleId(user.getRole() != null ? user.getRole().getId() : null)
                .roleName(user.getRole() != null ? user.getRole().getName() : null)
                .status(user.getStatus())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}