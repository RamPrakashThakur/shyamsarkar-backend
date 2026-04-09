package com.shyamsarkar.buildingmaterials.controller;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.shyamsarkar.buildingmaterials.dto.AuthRequest;
import com.shyamsarkar.buildingmaterials.dto.AuthResponse;
import com.shyamsarkar.buildingmaterials.dto.ForgotPasswordRequestDto;
import com.shyamsarkar.buildingmaterials.dto.RegisterRequestDto;
import com.shyamsarkar.buildingmaterials.dto.ResetPasswordRequestDto;
import com.shyamsarkar.buildingmaterials.entity.Role;
import com.shyamsarkar.buildingmaterials.entity.User;
import com.shyamsarkar.buildingmaterials.repository.UserRepository;
import com.shyamsarkar.buildingmaterials.service.AuthService;
import com.shyamsarkar.buildingmaterials.service.EmailService;
import com.shyamsarkar.buildingmaterials.service.UserService;
import com.shyamsarkar.buildingmaterials.util.JwtUtil;

import lombok.RequiredArgsConstructor;



@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor

public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository; 
    private final AuthService authService;
    private final EmailService emailService;

    

    @PostMapping("/register/customer")
    public ResponseEntity<String> registerCustomer(
            @RequestBody RegisterRequestDto dto) {

        authService.registerCustomer(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Customer registered successfully");
    }

    // ================= ADMIN REGISTER =================
    @PostMapping("/register/admin")
    public ResponseEntity<String> registerAdmin(
            @RequestBody RegisterRequestDto dto) {

        authService.registerAdmin(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Admin registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @RequestBody AuthRequest request) {

        User user = userService.getByEmail(request.getEmail());

        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String token = jwtUtil.generateToken(
                user.getEmail(),
                user.getRole()
        );

        return ResponseEntity.ok(
                new AuthResponse(token,user.getEmail(),user.getRole()));
    }

    @PostMapping("/create-admin")
    public ResponseEntity<String> createAdmin() {
        try {
            String adminEmail = "admin@shop.com";

            if (userRepository.existsByEmail(adminEmail)) {
                User existingAdmin = userRepository.findByEmail(adminEmail).orElse(null);
                if (existingAdmin != null) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("Admin already exists with role: " + existingAdmin.getRole());
                }
            }

            if (passwordEncoder == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("PasswordEncoder not configured properly");
            }

            // Create admin user
            User admin = new User();
            admin.setName("Admin User");
            admin.setEmail("admin@shop.com");
            admin.setPassword(passwordEncoder.encode("123456789"));
            admin.setRole(Role.ADMIN);
            admin.setAddress("ADMIN");     // ✅ REQUIRED
            admin.setPhone("123456789");  // ✅ if phone is also NOT NULL

            userRepository.save(admin);

            return ResponseEntity.ok("Admin created successfully with role: " + admin.getRole());

        } catch (Exception e) {
            e.printStackTrace(); // Logs the exact exception in console
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to create admin: " + e.getMessage());
        }
    }

    // ================= FORGOT PASSWORD =================
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(
            @RequestBody ForgotPasswordRequestDto dto
    ) {

        User user = userRepository
                .findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("Email not found"));

        String token = UUID.randomUUID().toString();

        user.setResetToken(token);
        user.setResetTokenExpiry(
                LocalDateTime.now().plusMinutes(15)
        );

        userRepository.save(user);

        emailService.sendResetToken(user.getEmail(), token);

        return ResponseEntity.ok().build();
    }

    // ================= RESET PASSWORD =================
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(
            @RequestBody ResetPasswordRequestDto dto
    ) {

        User user = userRepository
                .findByResetToken(dto.getToken())
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        if (user.getResetTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token expired");
        }

        user.setPassword(
                passwordEncoder.encode(dto.getNewPassword())
        );

        user.setResetToken(null);
        user.setResetTokenExpiry(null);

        userRepository.save(user);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/create-driver")
public ResponseEntity<String> createDriver() {
    try {
        String driverEmail = "driver@shop.com";

        if (userRepository.existsByEmail(driverEmail)) {
            User existingDriver = userRepository.findByEmail(driverEmail).orElse(null);
            if (existingDriver != null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Driver already exists with role: " + existingDriver.getRole());
            }
        }

        User driver = new User();
        driver.setName("Driver User");
        driver.setEmail(driverEmail);
        driver.setPassword(passwordEncoder.encode("987654321"));
        driver.setRole(Role.DRIVER);
        driver.setAddress("DRIVER");      // ✅ NOT NULL safe
        driver.setPhone("9999999999");    // ✅ NOT NULL safe

        userRepository.save(driver);

        return ResponseEntity.ok("Driver created successfully");

    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Failed to create driver: " + e.getMessage());
    }
}
    @PostMapping("/create-multiple-drivers")
public ResponseEntity<String> createMultipleDrivers() {

    try {
        for (int i = 1; i <= 20; i++) {

            String email = "driver" + i + "@shop.com";

            if (userRepository.existsByEmail(email)) {
                continue; // skip if already exists
            }

            User driver = new User();
            driver.setName(""); // 👈 blank (editable later)
            driver.setEmail(email);
            driver.setPassword(passwordEncoder.encode("123456"));
            driver.setRole(Role.DRIVER);

            driver.setPhone("");   // 👈 blank
            driver.setAddress(""); // 👈 blank

            userRepository.save(driver);
        }

        return ResponseEntity.ok("20 drivers created successfully 🚀");

    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error: " + e.getMessage());
    }
}

}
