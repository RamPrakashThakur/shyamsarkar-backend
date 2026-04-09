package com.shyamsarkar.buildingmaterials.service;



import com.shyamsarkar.buildingmaterials.dto.RegisterRequestDto;
import com.shyamsarkar.buildingmaterials.entity.User;

public interface UserService {
    void registerCustomer(RegisterRequestDto dto);
    User getByEmail(String email);
    User save(User user);
}
