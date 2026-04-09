package com.shyamsarkar.buildingmaterials.dto;

import lombok.Data;

@Data
public class DriverLocationDto {
    private Long orderId;
    private double lat;
    private double lng;
}
