package com.shyamsarkar.buildingmaterials.service;

import java.util.List;

import com.shyamsarkar.buildingmaterials.entity.Category;

public interface CategoryService {
    Category create(Category category);
    List<Category> getAll();
    Category getById(Long id);
}
