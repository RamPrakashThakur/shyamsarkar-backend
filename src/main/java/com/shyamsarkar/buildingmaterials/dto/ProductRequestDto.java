package com.shyamsarkar.buildingmaterials.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequestDto {

    @Schema(example = "UltraTech Cement")
    private String name;

    private Long categoryId;


    @Schema(example = "420")
    private double price;

    @Schema(example = "true")
    private boolean available;

    private int quantity;

    private Double transportCostPerKm;
}
