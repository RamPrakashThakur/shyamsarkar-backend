package com.shyamsarkar.buildingmaterials.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddToCartRequestDto {

    private Long productId;
    private int quantity;
    

}
