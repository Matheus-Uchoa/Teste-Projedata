package com.example.dto.productionsuggestion;

import java.math.BigDecimal;
import java.util.List;

public record ProductionSummaryDTO(
        List<ProductionSuggestionResponseDTO> suggestions,
        BigDecimal totalProductionValue
) {
}
