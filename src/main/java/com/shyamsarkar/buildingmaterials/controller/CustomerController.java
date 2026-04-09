package com.shyamsarkar.buildingmaterials.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;

import com.shyamsarkar.buildingmaterials.dto.CustomerRequestDto;
import com.shyamsarkar.buildingmaterials.dto.CustomerResponseDto;
import com.shyamsarkar.buildingmaterials.dto.ProfileResponseDto;
import com.shyamsarkar.buildingmaterials.entity.Customer;
import com.shyamsarkar.buildingmaterials.entity.Role;
import com.shyamsarkar.buildingmaterials.entity.User;
import com.shyamsarkar.buildingmaterials.service.CustomerService;
import com.shyamsarkar.buildingmaterials.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;
    private final UserService userService;

    // --- Customer CRUD endpoints (unchanged) ---
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerResponseDto create(@RequestBody CustomerRequestDto dto) {
        return map(customerService.create(dto));
    }

    @GetMapping("/{id}")
    public CustomerResponseDto get(@PathVariable Long id) {
        return map(customerService.getById(id));
    }

    @GetMapping
    public List<CustomerResponseDto> getAll() {
        return customerService.getAll()
                .stream()
                .map(this::map)
                .toList();
    }

    @PutMapping("/{id}")
    public CustomerResponseDto update(@PathVariable Long id, @RequestBody CustomerRequestDto dto) {
        return map(customerService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        customerService.delete(id);
    }

    @GetMapping("/email/{email}")
    public CustomerResponseDto getCustomerByEmail(@PathVariable String email) {

            Customer customer = customerService.getByEmail(email)
            .orElseThrow(() -> new RuntimeException(
                    "Customer not found with email: " + email
                ));

        return map(customer);
    }


    private CustomerResponseDto map(Customer c) {
        return new CustomerResponseDto(
                c.getId(),
                c.getName(),
                c.getPhone(),
                c.getAddress()
        );
    }

    // --- Fixed /profile endpoint ---
    @GetMapping("/profile")
@PreAuthorize("hasAnyRole('CUSTOMER','ADMIN','DRIVER')")
public ResponseEntity<?> getProfile(Authentication authentication) {
    try {
        String email = authentication.getName();
        User user = userService.getByEmail(email);

        ProfileResponseDto dto = new ProfileResponseDto(
            user.getName() != null ? user.getName() : "",
            user.getEmail() != null ? user.getEmail() : "",
            user.getPhone() != null ? user.getPhone() : "",
            user.getAddress() != null ? user.getAddress() : "",
            user.getRole() != null ? user.getRole() : Role.CUSTOMER // 👈 add Role
        );

        return ResponseEntity.ok(dto);
    } catch (Exception e) {
        e.printStackTrace(); // 🔥 This will show the real 500 cause
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "Something went wrong", "error", e.getMessage()));
    }
}

@PostMapping("/fcm-token")
public void saveToken(
        @RequestParam String token,
        Authentication auth
) {
    customerService.updateFcmToken(auth.getName(), token);
}
@PutMapping("/profile")
public ResponseEntity<ProfileResponseDto> updateProfile(
        @RequestBody ProfileResponseDto dto,
        Authentication auth
) {
    User user = userService.getByEmail(auth.getName());

    user.setName(dto.getName());
    user.setPhone(dto.getPhone());
    user.setAddress(dto.getAddress());

    userService.save(user);

    return ResponseEntity.ok(new ProfileResponseDto(
            user.getName(),
            user.getEmail(),
            user.getPhone(),
            user.getAddress(),
            user.getRole()
    ));
}
}
