package com.shyamsarkar.buildingmaterials.controller;

import com.shyamsarkar.buildingmaterials.dto.ProfileResponseDto;
import com.shyamsarkar.buildingmaterials.entity.Customer;
import com.shyamsarkar.buildingmaterials.entity.Role;
import com.shyamsarkar.buildingmaterials.entity.User;
import com.shyamsarkar.buildingmaterials.service.CustomerService;
import com.shyamsarkar.buildingmaterials.service.UserService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequiredArgsConstructor
public class ProfileController {

    private final CustomerService customerService;
    private final UserService userService;

    @GetMapping("/api/profile")
    public ResponseEntity<ProfileResponseDto> getProfile(Authentication authentication) {

        String email = authentication.getName();
        String authority = authentication.getAuthorities()
                .iterator()
                .next()
                .getAuthority(); // ROLE_ADMIN / ROLE_CUSTOMER

        // 🔹 CUSTOMER PROFILE
        if ("ROLE_CUSTOMER".equals(authority)) {

            Customer customer = customerService.getByEmail(email)
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Customer not found with email: " + email
                    ));

            return ResponseEntity.ok(
                    new ProfileResponseDto(
                            customer.getName(),
                            customer.getEmail(),
                            customer.getPhone(),
                            customer.getAddress(),
                            Role.CUSTOMER
                    )
            );
        }

        // 🔹 ADMIN PROFILE
        if ("ROLE_ADMIN".equals(authority)) {

            User user = userService.getByEmail(email);
            if (user == null) {
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Admin not found with email: " + email
                );
            }

            return ResponseEntity.ok(
                    new ProfileResponseDto(
                            user.getName(),
                            user.getEmail(),
                            user.getPhone(),
                            user.getAddress(),
                            Role.ADMIN
                    )
            );
        }

        // 🔹 UNKNOWN ROLE
        throw new ResponseStatusException(
                HttpStatus.FORBIDDEN,
                "Access denied"
        );
    }
    @PutMapping("/api/profile")
public ProfileResponseDto updateProfile(
        @RequestBody ProfileResponseDto dto,
        Authentication authentication
) {
    String email = authentication.getName();

    Customer customer = customerService.getByEmail(email)
            .orElseThrow(() -> new RuntimeException("Customer not found"));

    customer.setName(dto.getName());
    customer.setPhone(dto.getPhone());
    customer.setAddress(dto.getAddress());

    customerService.save(customer);

    return new ProfileResponseDto(
            customer.getName(),
            customer.getEmail(),
            customer.getPhone(),
            customer.getAddress(),
            Role.CUSTOMER
    );
}
}
