package com.shyamsarkar.buildingmaterials.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.shyamsarkar.buildingmaterials.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    boolean existsByName(String name);
}

