package com.shyamsarkar.buildingmaterials.dto;

import com.shyamsarkar.buildingmaterials.entity.OrderItem;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemResponseDto {
    private String productName;
    private int quantity;
    private double price;

    public static OrderItemResponseDto from(OrderItem item) {
        return new OrderItemResponseDto(
                item.getProduct().getName(),
                item.getQuantity(),
                item.getPriceAtPurchase()
        );
    }
}

