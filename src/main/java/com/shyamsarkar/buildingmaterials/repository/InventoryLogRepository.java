package com.shyamsarkar.buildingmaterials.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shyamsarkar.buildingmaterials.entity.InventoryLog;

public interface InventoryLogRepository extends JpaRepository<InventoryLog, Long> {
}
