package com.shyamsarkar.buildingmaterials.repository;

import java.util.List;
import java.util.Optional;



import org.springframework.data.jpa.repository.JpaRepository;

import com.shyamsarkar.buildingmaterials.entity.Role;
import com.shyamsarkar.buildingmaterials.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    // ✅ ADD THIS
    List<User> findByRole(Role role);

    Optional<User> findByResetToken(String resetToken);
}
