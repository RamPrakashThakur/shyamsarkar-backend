package com.shyamsarkar.buildingmaterials.entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter @Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    
    private String email;
    
    @Column(unique = true,nullable = false)
    private String phone;

    @Column(nullable = false)
    private String address;

    
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role; // CUSTOMER / ADMIN

    @JsonIgnore
    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    private Customer customer;

    private String resetToken;

    
    private LocalDateTime resetTokenExpiry;
}

