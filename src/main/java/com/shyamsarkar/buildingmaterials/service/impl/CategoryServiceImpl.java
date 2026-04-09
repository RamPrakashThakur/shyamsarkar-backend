package com.shyamsarkar.buildingmaterials.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.shyamsarkar.buildingmaterials.entity.Category;
import com.shyamsarkar.buildingmaterials.exception.ResourceNotFoundException;
import com.shyamsarkar.buildingmaterials.repository.CategoryRepository;
import com.shyamsarkar.buildingmaterials.service.CategoryService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public Category create(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public List<Category> getAll() {
        return categoryRepository.findAll();
    }

    @Override
public Category getById(Long id) {
    return categoryRepository.findById(id)
            .orElseThrow(() ->
                    new ResourceNotFoundException(
                            "Category not found with id " + id
                    )
            );
}
}
