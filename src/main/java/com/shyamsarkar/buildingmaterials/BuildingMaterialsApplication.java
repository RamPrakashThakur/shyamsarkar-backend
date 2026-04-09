package com.shyamsarkar.buildingmaterials;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.shyamsarkar.buildingmaterials")
public class BuildingMaterialsApplication {

    public static void main(String[] args) {
        SpringApplication.run(BuildingMaterialsApplication.class, args);
    }
}