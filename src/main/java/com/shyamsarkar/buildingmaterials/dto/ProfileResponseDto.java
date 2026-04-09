package com.shyamsarkar.buildingmaterials.dto;

import com.shyamsarkar.buildingmaterials.entity.Role;

public class ProfileResponseDto {

    private String name;
    private String email;
    private String phone;
    private String address;
    private Role role;   // ✅ Role as class

    public ProfileResponseDto(
            String name,
            String email,
            String phone,
            String address,
            Role role
    ) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public Role getRole() {
        return role;
    }
}
