package com.example.dto.productionsuggestion;

import java.math.BigDecimal;

public record ProductionSuggestionResponseDTO(
        Long productId,
        String productName,
        BigDecimal productValue,
        Long suggestedQuantity,
        BigDecimal totalValue,
        Long priorityRank
) {
}
