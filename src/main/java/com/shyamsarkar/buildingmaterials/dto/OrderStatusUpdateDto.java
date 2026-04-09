package com.shyamsarkar.buildingmaterials.dto;

import com.shyamsarkar.buildingmaterials.entity.OrderStatus;
import lombok.Data;

@Data
public class OrderStatusUpdateDto {
    private OrderStatus status;
}

