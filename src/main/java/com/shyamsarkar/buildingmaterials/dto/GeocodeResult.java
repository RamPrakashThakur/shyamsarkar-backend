package com.shyamsarkar.buildingmaterials.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GeocodeResult {
    private double lat;
    private double lng;
    private String district;
    private String state;
}