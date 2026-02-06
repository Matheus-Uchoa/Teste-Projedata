package com.example.dto.rawmaterial;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record RawMaterialRequestDTO(
        @NotBlank(message = "Name is required")
        String name,

        @NotNull(message = "Stock quantity is required")
        @DecimalMin(value = "0.0", message = "Stock quantity must be greater than or equal to zero")
        BigDecimal stockQuantity
) {
}
