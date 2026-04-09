package com.shyamsarkar.buildingmaterials.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Building Materials API",
                version = "1.0",
                description = "Backend APIs for Building Materials Shop"
        )
)
public class OpenApiConfig {
}
