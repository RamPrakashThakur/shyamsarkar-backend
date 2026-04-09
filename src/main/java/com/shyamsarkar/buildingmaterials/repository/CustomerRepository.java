package com.shyamsarkar.buildingmaterials.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shyamsarkar.buildingmaterials.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByPhone(String phone);

    boolean existsByPhone(String phone);

    Optional<Customer> findByEmail(String email);
}

