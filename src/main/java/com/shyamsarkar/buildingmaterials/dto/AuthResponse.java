package com.shyamsarkar.buildingmaterials.dto;

import com.shyamsarkar.buildingmaterials.entity.Role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter 
public class AuthResponse {

    private String token;
    private String email;
    private Role role;
    


    
}
