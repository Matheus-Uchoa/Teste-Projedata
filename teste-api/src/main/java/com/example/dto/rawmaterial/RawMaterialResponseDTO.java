package com.example.dto.rawmaterial;

import java.math.BigDecimal;

public record RawMaterialResponseDTO(
        Long id,
        String name,
        BigDecimal stockQuantity
) {
}
