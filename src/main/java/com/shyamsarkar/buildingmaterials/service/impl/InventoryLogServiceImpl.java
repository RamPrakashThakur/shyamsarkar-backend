package com.shyamsarkar.buildingmaterials.service.impl;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.shyamsarkar.buildingmaterials.entity.InventoryLog;
import com.shyamsarkar.buildingmaterials.entity.Product;
import com.shyamsarkar.buildingmaterials.repository.InventoryLogRepository;
import com.shyamsarkar.buildingmaterials.service.InventoryLogService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InventoryLogServiceImpl implements InventoryLogService {

    private final InventoryLogRepository inventoryLogRepository;

    @Override
    public void log(Product product, int quantityChange, String reason) {

        //inventory change in OrderServiceImpl   createOrder() and cancelOrder()
        //and in ProductServiceImpl in updateStock()

        InventoryLog log = new InventoryLog();
        log.setProduct(product);
        log.setQuantityChange(quantityChange);
        log.setReason(reason);
        log.setCreatedAt(LocalDateTime.now());

        inventoryLogRepository.save(log);
    }
}
