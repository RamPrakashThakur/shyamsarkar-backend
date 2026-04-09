package com.shyamsarkar.buildingmaterials.dto;

import lombok.Data;
import lombok.Getter;
import lombok.*;

@Data
@Getter @Setter
public class RegisterRequestDto {
    private String name;
    private String email;
    private String phone;
    private String address;
    private String password;
}