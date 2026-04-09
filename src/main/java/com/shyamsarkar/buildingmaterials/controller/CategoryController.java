package com.shyamsarkar.buildingmaterials.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.shyamsarkar.buildingmaterials.dto.CategoryRequestDto;
import com.shyamsarkar.buildingmaterials.dto.CategoryResponseDto;
import com.shyamsarkar.buildingmaterials.entity.Category;
import com.shyamsarkar.buildingmaterials.service.CategoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService service;

    // 🔥 ADMIN CREATE
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public CategoryResponseDto create(@RequestBody CategoryRequestDto dto) {

        Category category = new Category(null, dto.getName());

        Category saved = service.create(category);

        return new CategoryResponseDto(
                saved.getId(),
                saved.getName()
        );
    }

    // 🔥 GET ALL (for spinner)
    @GetMapping
    public List<CategoryResponseDto> getAll() {

        return service.getAll()
                .stream()
                .map(c -> new CategoryResponseDto(c.getId(), c.getName()))
                .toList();
    }
}

