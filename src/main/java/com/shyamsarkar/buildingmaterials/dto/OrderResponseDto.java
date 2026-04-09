package com.shyamsarkar.buildingmaterials.dto;

import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

import com.shyamsarkar.buildingmaterials.entity.Order;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponseDto {

    private Long id;

    private Long customerId;
    private String customerName;
    private String customerPhone;

    private double totalAmount;
    private String status;
    private LocalDateTime createdAt;
    private List<OrderItemResponseDto> items;
    private String deliveryAddress;

    public static OrderResponseDto from(Order order) {
        return new OrderResponseDto(
                order.getId(),
                order.getCustomer().getId(),
                order.getCustomer().getName(),
                order.getCustomer().getPhone(),
                order.getTotalAmount(),
                order.getStatus().name(),
                order.getCreatedAt(),
                order.getItems()
                        .stream()
                        .map(OrderItemResponseDto::from)
                        .toList(),
                order.getDeliveryAddress()

        );
    }
}
