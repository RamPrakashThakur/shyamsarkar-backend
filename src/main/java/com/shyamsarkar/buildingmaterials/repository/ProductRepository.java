package com.shyamsarkar.buildingmaterials.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shyamsarkar.buildingmaterials.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
}