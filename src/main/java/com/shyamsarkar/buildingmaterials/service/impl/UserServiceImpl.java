package com.shyamsarkar.buildingmaterials.service.impl;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.shyamsarkar.buildingmaterials.dto.RegisterRequestDto;
import com.shyamsarkar.buildingmaterials.entity.Role;
import com.shyamsarkar.buildingmaterials.entity.User;
import com.shyamsarkar.buildingmaterials.repository.UserRepository;
import com.shyamsarkar.buildingmaterials.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Constructor injection (no Lombok)
    public UserServiceImpl(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void registerCustomer(RegisterRequestDto dto) {

        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setAddress(dto.getAddress());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(Role.CUSTOMER);

        userRepository.save(user);
    }

    @Override
    public User getByEmail(String email) {

        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        return optionalUser.get();
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }
}
