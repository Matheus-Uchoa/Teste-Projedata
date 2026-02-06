package com.example.dto.productrawmaterial;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ProductRawMaterialRequestDTO(
        @NotNull(message = "Raw material ID is required")
        Long rawMaterialId,

        @NotNull(message = "Quantity needed is required")
        @DecimalMin(value = "0.01", message = "Quantity needed must be greater than zero")
        BigDecimal quantityNeeded
) {
}
