package com.shyamsarkar.buildingmaterials.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.shyamsarkar.buildingmaterials.dto.RegisterRequestDto;
import com.shyamsarkar.buildingmaterials.entity.Customer;
import com.shyamsarkar.buildingmaterials.entity.Role;
import com.shyamsarkar.buildingmaterials.entity.User;
import com.shyamsarkar.buildingmaterials.repository.CustomerRepository;
import com.shyamsarkar.buildingmaterials.repository.UserRepository;
import com.shyamsarkar.buildingmaterials.service.AuthService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    // ================= CUSTOMER REGISTER =================
    @Override
    public void registerCustomer(RegisterRequestDto dto) {
        registerUser(dto, Role.CUSTOMER);
    }

    // ================= ADMIN REGISTER =================
    @Override
    public void registerAdmin(RegisterRequestDto dto) {
        registerUser(dto, Role.ADMIN);
    }

    // ================= COMMON METHOD =================
    private void registerUser(RegisterRequestDto dto, Role role) {

        // 🔐 Save User
        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setPhone(dto.getPhone());
        user.setAddress(dto.getAddress());
        // 🔥 AUTO ROLE DETECTION (ADD HERE)
    if (dto.getEmail().equalsIgnoreCase("admin@shop.com")) {
        user.setRole(Role.ADMIN);
    } else if (dto.getEmail().startsWith("driver")) {
        user.setRole(Role.DRIVER);
    } else {
        user.setRole(Role.CUSTOMER);
    }

    userRepository.save(user);

        // 👤 Create Customer ONLY if role = CUSTOMER
        if (role == Role.CUSTOMER) {
            Customer customer = new Customer();
            customer.setUser(user);        // 🔥 mandatory
            customer.setName(user.getName());
            customer.setEmail(user.getEmail());
            customer.setPhone(user.getPhone());
            customer.setAddress(user.getAddress());

            customerRepository.save(customer);
        }
    }
}
