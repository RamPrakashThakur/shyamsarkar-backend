package com.shyamsarkar.buildingmaterials.dto;

import lombok.Data;

@Data
public class CartItemResponseDto {

    private Long productId;
    private String productName;
    private String imageUrl;
    private int quantity;
    private double price;

    private double itemTotal;
    private double transportCost; // 👈 per item
}