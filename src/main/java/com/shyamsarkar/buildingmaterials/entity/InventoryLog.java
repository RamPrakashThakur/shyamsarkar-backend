package com.shyamsarkar.buildingmaterials.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "inventory_logs")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventoryLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Product product;

    private int quantityChange; // + or -

    private String reason; // ORDER_PLACED, ORDER_CANCELLED, MANUAL_UPDATE

    private LocalDateTime createdAt;
}

