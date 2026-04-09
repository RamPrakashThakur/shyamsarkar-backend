package com.shyamsarkar.buildingmaterials.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DistanceResult {

    private double distanceKm;
    private long durationMinutes;
}