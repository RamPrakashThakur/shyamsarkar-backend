package com.shyamsarkar.buildingmaterials.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;
    

    @ManyToOne
    @JoinColumn(name = "category_id",nullable = false)
    private Category category;

    private double price;
    private boolean available;

    @Column(nullable = false)
    private int quantity;


    private String imageUrl; 

    private double transportCostPerKm;
}