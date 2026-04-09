package com.shyamsarkar.buildingmaterials.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "customers")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String phone;

    private String address;

    private String email;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String fcmToken;

}
