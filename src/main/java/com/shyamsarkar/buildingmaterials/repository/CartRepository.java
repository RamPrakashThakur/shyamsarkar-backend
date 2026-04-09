package com.shyamsarkar.buildingmaterials.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shyamsarkar.buildingmaterials.entity.Cart;
import com.shyamsarkar.buildingmaterials.entity.Customer;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByCustomer(Customer customer);
    Optional<Cart> findByCustomerId(Long customerId);
}