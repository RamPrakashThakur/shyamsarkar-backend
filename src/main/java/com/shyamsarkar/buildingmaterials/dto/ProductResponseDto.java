package com.shyamsarkar.buildingmaterials.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponseDto {

    private Long id;
    private String name;
    private String category;
    private Long categoryId; 
    private double price;
    private boolean available;
    private int quantity;
    private String imageUrl; 
    private Double transportCostPerKm; 
}
