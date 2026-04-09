package com.shyamsarkar.buildingmaterials.controller;

import java.util.List;

import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shyamsarkar.buildingmaterials.dto.ProductRequestDto;
import com.shyamsarkar.buildingmaterials.dto.ProductResponseDto;
import com.shyamsarkar.buildingmaterials.dto.StockUpdateDto;
import com.shyamsarkar.buildingmaterials.entity.Category;
import com.shyamsarkar.buildingmaterials.entity.Product;
import com.shyamsarkar.buildingmaterials.service.*;

import org.slf4j.Logger;


import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final ImageStorageService imageStorageService;


    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);


    // ✅ ADMIN – Update transport cost per km
    @PutMapping("/{id}/transport-cost")
@PreAuthorize("hasRole('ADMIN')")
public Product updateTransportCost(
        @PathVariable Long id,
        @RequestParam double costPerKm
) {
    return productService.updateTransportCost(id, costPerKm);
}


// inside createProductWithImage()
@PostMapping(
        value = "/with-image",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE
)
public ProductResponseDto createProductWithImage(
        @RequestPart("image") MultipartFile image,
        @RequestPart("data") String data) throws Exception {

    logger.info("Received request to create product with image: {}", image.getOriginalFilename());

    try {
        ObjectMapper mapper = new ObjectMapper();
        ProductRequestDto dto = mapper.readValue(data, ProductRequestDto.class);
        logger.info("Parsed product DTO: {}", dto);

        Category category = categoryService.getById(dto.getCategoryId());
        logger.info("Fetched category: {}", category.getName());

        String imageUrl = imageStorageService.saveImage(image); // <-- log inside service too
        logger.info("Image saved at: {}", imageUrl);

        Product product = new Product(
                null,
                dto.getName(),
                category,
                dto.getPrice(),
                dto.isAvailable(),
                dto.getQuantity(),
                imageUrl,
                dto.getTransportCostPerKm()
        );

        Product saved = productService.createProduct(product);
        logger.info("Product saved with ID: {}", saved.getId());

        return new ProductResponseDto(
                saved.getId(),
                saved.getName(),
                saved.getCategory().getName(),
                saved.getCategory().getId(),
                saved.getPrice(),
                saved.isAvailable(),
                saved.getQuantity(),
                saved.getImageUrl(),
                saved.getTransportCostPerKm()
        );

    } catch (Exception e) {
        logger.error("Error creating product with image", e);
        throw e; // so Spring shows 500 + full stack trace
    }
}



    // CREATE PRODUCT
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponseDto createProduct(
            @RequestBody ProductRequestDto dto) {

        Category category = categoryService.getById(dto.getCategoryId());

        Product product = new Product(
                null,
                dto.getName(),
                category,
                dto.getPrice(),
                dto.isAvailable(),
                dto.getQuantity(),
                null,
                dto.getTransportCostPerKm()
        );

        Product saved = productService.createProduct(product);
        return mapToResponse(saved);
    }

    // GET ALL PRODUCTS
    @GetMapping
    public List<ProductResponseDto> getAllProducts() {
        return productService.getAllProducts()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // GET PRODUCT BY ID
    @GetMapping("/{id}")
    public ProductResponseDto getProduct(@PathVariable Long id) {
        return mapToResponse(productService.getProductById(id));
    }

    // UPDATE PRODUCT
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
     public ProductResponseDto updateProduct(
        @PathVariable Long id,
        @RequestBody ProductRequestDto dto) {

           Product updated = productService.updateProduct(id, dto);
          return mapToResponse(updated);
    }

    // DELETE PRODUCT
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }

    // RESPONSE MAPPER
    private ProductResponseDto mapToResponse(Product product) {
        return new ProductResponseDto(
                product.getId(),
                product.getName(),
                product.getCategory().getName(), 
                product.getCategory().getId(),// ✅ clean API
                product.getPrice(),
                product.isAvailable(),
                product.getQuantity(),
                product.getImageUrl(),
                product.getTransportCostPerKm()
        );
    }

    //UPDATE STOCK
    @PutMapping("/{id}/stock")
    public ProductResponseDto updateStock(
        @PathVariable Long id,
        @RequestBody StockUpdateDto dto) {

            Product updated = productService.updateStock(id, dto.getQuantity());
            return mapToResponse(updated);
        }
}
