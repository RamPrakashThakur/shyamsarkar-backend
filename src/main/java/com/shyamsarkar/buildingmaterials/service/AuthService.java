package com.shyamsarkar.buildingmaterials.service;

import com.shyamsarkar.buildingmaterials.dto.RegisterRequestDto;

public interface AuthService {

    void registerCustomer(RegisterRequestDto dto);

    void registerAdmin(RegisterRequestDto dto);
}
