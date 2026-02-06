package com.example.dto.product;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ProductRequestDTO(
        @NotBlank(message = "Name is required")
        String name,

        @NotNull(message = "Value is required")
        @DecimalMin(value = "0.01", message = "Value must be greater than zero")
        BigDecimal value
) {
}
