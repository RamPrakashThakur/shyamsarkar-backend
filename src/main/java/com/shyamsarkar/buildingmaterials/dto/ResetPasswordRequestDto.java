package com.shyamsarkar.buildingmaterials.dto;

import lombok.Data;

@Data
public class ResetPasswordRequestDto {
    private String token;
    private String newPassword;
}
