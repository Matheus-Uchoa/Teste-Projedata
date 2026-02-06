package com.example.dto.productrawmaterial;

import java.math.BigDecimal;

public record ProductRawMaterialResponseDTO(
        Long id,
        Long productId,
        String productName,
        Long rawMaterialId,
        String rawMaterialName,
        BigDecimal quantityNeeded
) {
}
