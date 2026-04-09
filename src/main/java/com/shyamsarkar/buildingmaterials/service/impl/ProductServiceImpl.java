package com.shyamsarkar.buildingmaterials.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shyamsarkar.buildingmaterials.dto.ProductRequestDto;
import com.shyamsarkar.buildingmaterials.entity.Category;
import com.shyamsarkar.buildingmaterials.entity.Product;
import com.shyamsarkar.buildingmaterials.exception.BadRequestException;
import com.shyamsarkar.buildingmaterials.exception.ResourceNotFoundException;
import com.shyamsarkar.buildingmaterials.repository.ProductRepository;
import com.shyamsarkar.buildingmaterials.service.CategoryService;
import com.shyamsarkar.buildingmaterials.service.InventoryLogService;
import com.shyamsarkar.buildingmaterials.service.ProductService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService {

    private final InventoryLogService inventoryLogService;

    private final ProductRepository productRepository;

    private final CategoryService categoryService;

    // ================= UPDATE STOCK =================
    @Override
    @Transactional
    public Product updateStock(Long id, int quantity) {

        if (quantity < 0) {
            throw new BadRequestException("Stock quantity cannot be negative");
        }

        Product product = productRepository.findById(id)
            .orElseThrow(() ->
                    new ResourceNotFoundException(
                            "Product not found with id " + id
                    )
            );

        int oldQuantity = product.getQuantity();
        int difference = quantity - oldQuantity;

        product.setQuantity(quantity);

    // ✅ Auto availability logic
        product.setAvailable(quantity > 0);

        Product savedProduct = productRepository.save(product);

    // ✅ INVENTORY LOG (ONLY if stock changed)
        if (difference != 0) {
            inventoryLogService.log(
                savedProduct,
                difference,               // + or - automatically handled
                "MANUAL_STOCK_UPDATE"
            );
        }

        return savedProduct; 
    }



    // ================= CREATE PRODUCT =================
    @Override
    public Product createProduct(Product product) {

        if (product.getPrice() < 0) {
            throw new BadRequestException("Product price cannot be negative");
        }

        if (product.getQuantity() < 0) {
            throw new BadRequestException("Product quantity cannot be negative");
        }

        product.setAvailable(product.getQuantity() > 0);

        return productRepository.save(product);
    }

    // ================= UPDATE PRODUCT =================
    @Override
public Product updateProduct(Long id, ProductRequestDto dto) {

    Product existing = productRepository.findById(id)
            .orElseThrow(() ->
                    new ResourceNotFoundException(
                            "Product not found with id " + id
                    )
            );

    if (dto.getPrice() < 0) {
        throw new BadRequestException("Product price cannot be negative");
    }

    Category category =
            categoryService.getById(dto.getCategoryId());

    existing.setName(dto.getName());
    existing.setCategory(category);
    existing.setPrice(dto.getPrice());
    existing.setAvailable(dto.isAvailable());
    existing.setQuantity(dto.getQuantity());
    if (dto.getTransportCostPerKm() != null) {
      existing.setTransportCostPerKm(dto.getTransportCostPerKm());
    }

    return productRepository.save(existing);
}

    // ================= GET PRODUCT =================
    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Product not found with id " + id
                        )
                );
    }

    // ================= GET ALL =================
    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // ================= DELETE =================
    @Override
    public void deleteProduct(Long id) {

        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                    "Product not found with id " + id
            );
        }

        productRepository.deleteById(id);
    }

    @Override
    public Product updateTransportCost(Long productId, double costPerKm) {
        Product product = getProductById(productId);
        product.setTransportCostPerKm(costPerKm);
        return productRepository.save(product);
    }

    @Override
    public Product save(Product product) {
      return productRepository.save(product);
}
}
