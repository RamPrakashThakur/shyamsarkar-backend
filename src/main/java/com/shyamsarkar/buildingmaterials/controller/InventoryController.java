package com.shyamsarkar.buildingmaterials.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.shyamsarkar.buildingmaterials.entity.InventoryLog;
import com.shyamsarkar.buildingmaterials.repository.InventoryLogRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryLogRepository inventoryLogRepository;

    @GetMapping("/logs")
    public List<InventoryLog> getAllLogs() {
        return inventoryLogRepository.findAll();
    }
}

