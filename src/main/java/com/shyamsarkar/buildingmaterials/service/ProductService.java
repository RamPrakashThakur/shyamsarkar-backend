package com.shyamsarkar.buildingmaterials.service;

import java.util.List;

import com.shyamsarkar.buildingmaterials.dto.ProductRequestDto;
import com.shyamsarkar.buildingmaterials.entity.Product;

public interface ProductService {

    Product createProduct(Product product);

    Product updateProduct(Long id, ProductRequestDto product);

    Product getProductById(Long id);

    List<Product> getAllProducts();

    void deleteProduct(Long id);

    Product updateStock(Long id, int quantity);

    Product updateTransportCost(Long productId, double costPerKm);

    Product save(Product product);
}
