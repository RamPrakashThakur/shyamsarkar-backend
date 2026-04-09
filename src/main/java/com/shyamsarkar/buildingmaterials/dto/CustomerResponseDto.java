package com.shyamsarkar.buildingmaterials.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerResponseDto {

    private Long id;
    private String name;
    private String phone;
    private String address;
}
