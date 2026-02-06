package com.example.service;

import com.example.dto.PageResponse;
import com.example.dto.productionsuggestion.ProductionSuggestionResponseDTO;
import com.example.entity.ProductionSuggestion;
import com.example.mapper.ProductionSuggestionMapper;
import com.example.repository.ProductionSuggestionRepository;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class ProductionSuggestionService {

    @Inject
    ProductionSuggestionRepository productionSuggestionRepository;

    public PageResponse<ProductionSuggestionResponseDTO> getProductionSuggestions(
            int pageNumber,
            int pageSize,
            String searchName,
            String sortDirection) {

        long count = productionSuggestionRepository.count(searchName);

        List<ProductionSuggestion> suggestions = productionSuggestionRepository
                .findAllOrderedByPriority(Page.of(pageNumber, pageSize), searchName, sortDirection);

        List<ProductionSuggestionResponseDTO> suggestionDTOs = suggestions.stream()
                .map(ProductionSuggestionMapper::toResponseDTO)
                .collect(Collectors.toList());

        return new PageResponse<>(
                suggestionDTOs,
                pageNumber,
                pageSize,
                count
        );
    }
}
