package com.shyamsarkar.buildingmaterials.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemRequestDto {
    private Long productId;
    private int quantity;
}
