package com.shyamsarkar.buildingmaterials.service;

import com.shyamsarkar.buildingmaterials.dto.ProfileResponseDto;
import com.shyamsarkar.buildingmaterials.entity.Customer;
import com.shyamsarkar.buildingmaterials.entity.Role;
import com.shyamsarkar.buildingmaterials.entity.User;
import com.shyamsarkar.buildingmaterials.repository.CustomerRepository;
import com.shyamsarkar.buildingmaterials.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;

    public ProfileResponseDto getProfile(String email) {

        // 🔹 Fetch logged-in user
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "User not found with email: " + email
                        )
                );

        // 🔹 ADMIN PROFILE (from User table)
        if ("ADMIN".equals(user.getRole())) {
            return new ProfileResponseDto(
                    user.getName(),
                    user.getEmail(),
                    user.getPhone(),
                    user.getAddress(),
                    Role.ADMIN
                );

        }

        // 🔹 CUSTOMER PROFILE (from Customer table)
        if ("CUSTOMER".equals(user.getRole())) {

            Customer customer = customerRepository.findByEmail(email)
                    .orElseThrow(() ->
                            new ResponseStatusException(
                                    HttpStatus.NOT_FOUND,
                                    "Customer profile not created for email: " + email
                            )
                    );

            return new ProfileResponseDto(
                    customer.getName(),
                    customer.getEmail(),
                    customer.getPhone(),
                    customer.getAddress(),
                    Role.CUSTOMER
            );
        }

        // 🔹 Fallback (should never happen)
        throw new ResponseStatusException(
                HttpStatus.FORBIDDEN,
                "Invalid role for user: " + user.getRole()
        );
    }
}

