package com.shyamsarkar.buildingmaterials.dto;

import lombok.Data;
import java.util.List;

@Data
public class CartResponseDto {

    private List<CartItemResponseDto> items;

    private double itemsTotal;
    private double transportationCost;
    private double grandTotal;
}