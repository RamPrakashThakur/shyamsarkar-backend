package com.shyamsarkar.buildingmaterials.dto;

import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequestDto {
    
    
    private String deliveryAddress;

    private Double deliveryLat;
    private Double deliveryLng;
}

