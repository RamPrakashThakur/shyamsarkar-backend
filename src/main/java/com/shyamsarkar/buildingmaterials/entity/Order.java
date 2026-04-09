package com.shyamsarkar.buildingmaterials.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter @Setter
@Table(name = "orders")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double itemsAmount; 

    private double totalAmount;

    @Column(nullable = false, length = 500)
    private String deliveryAddress;
    

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> items;

    @ManyToOne
    @JoinColumn(name = "driver_id")
    private User driver;

    private double deliveryCharge;
    private double deliveryDistanceKm;
}

