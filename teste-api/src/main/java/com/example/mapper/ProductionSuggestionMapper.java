package com.example.mapper;

import com.example.dto.productionsuggestion.ProductionSuggestionResponseDTO;
import com.example.entity.ProductionSuggestion;

public class ProductionSuggestionMapper {

    public static ProductionSuggestionResponseDTO toResponseDTO(ProductionSuggestion entity) {
        return new ProductionSuggestionResponseDTO(
                entity.getProductId(),
                entity.getProductName(),
                entity.getProductValue(),
                entity.getSuggestedQuantity(),
                entity.getTotalValue(),
                entity.getPriorityRank()
        );
    }
}
