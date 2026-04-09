package com.shyamsarkar.buildingmaterials.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerRequestDto {

    private String name;
    private String phone;
    private String address;
    private String email;
}
