package com.shyamsarkar.buildingmaterials.service;

import com.shyamsarkar.buildingmaterials.entity.Product;

public interface InventoryLogService {

    void log(Product product, int quantityChange, String reason);
}
