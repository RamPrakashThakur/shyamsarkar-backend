package com.shyamsarkar.buildingmaterials.dto;

import com.shyamsarkar.buildingmaterials.entity.User;
import com.shyamsarkar.buildingmaterials.entity.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {

    private Long id;
    private String name;
    private String email;
    private Role role;

    public static UserResponseDto from(User user) {
        return new UserResponseDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole()
        );
    }
}
