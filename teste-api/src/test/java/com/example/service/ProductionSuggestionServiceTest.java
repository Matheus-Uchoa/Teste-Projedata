package com.example.service;

import com.example.dto.PageResponse;
import com.example.dto.productionsuggestion.ProductionSuggestionResponseDTO;
import com.example.entity.ProductionSuggestion;
import com.example.repository.ProductionSuggestionRepository;
import io.quarkus.panache.common.Page;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.InjectMock;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@QuarkusTest
class ProductionSuggestionServiceTest {

    @Inject
    ProductionSuggestionService productionSuggestionService;

    @InjectMock
    ProductionSuggestionRepository productionSuggestionRepository;

    private List<ProductionSuggestion> testSuggestions;

    @BeforeEach
    void setUp() {
        Mockito.reset(productionSuggestionRepository);

        ProductionSuggestion suggestion1 = new ProductionSuggestion();
        suggestion1.setProductId(1L);
        suggestion1.setProductName("Product A");
        suggestion1.setProductValue(new BigDecimal("10.00"));
        suggestion1.setSuggestedQuantity(100L);
        suggestion1.setTotalValue(new BigDecimal("1000.00"));
        suggestion1.setPriorityRank(1L);

        ProductionSuggestion suggestion2 = new ProductionSuggestion();
        suggestion2.setProductId(2L);
        suggestion2.setProductName("Product B");
        suggestion2.setProductValue(new BigDecimal("20.00"));
        suggestion2.setSuggestedQuantity(25L);
        suggestion2.setTotalValue(new BigDecimal("500.00"));
        suggestion2.setPriorityRank(2L);

        testSuggestions = Arrays.asList(suggestion1, suggestion2);
    }

    @Test
    void testGetProductionSuggestions() {
        // Arrange
        when(productionSuggestionRepository.count(anyString())).thenReturn(2L);
        when(productionSuggestionRepository.findAllOrderedByPriority(any(Page.class), anyString(), anyString()))
            .thenReturn(testSuggestions);

        // Act
        PageResponse<ProductionSuggestionResponseDTO> result =
            productionSuggestionService.getProductionSuggestions(0, 10, "", "");

        // Assert
        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        assertEquals(2L, result.getTotalElements());
        assertEquals(1, result.getTotalPages());

        ProductionSuggestionResponseDTO first = result.getContent().get(0);
        assertEquals("Product A", first.productName());
        assertEquals(100L, first.suggestedQuantity());
        assertEquals(new BigDecimal("1000.00"), first.totalValue());
        assertEquals(1L, first.priorityRank());
    }

    @Test
    void testGetProductionSuggestionsEmpty() {
        // Arrange
        when(productionSuggestionRepository.count(anyString())).thenReturn(0L);
        when(productionSuggestionRepository.findAllOrderedByPriority(any(Page.class), anyString(), anyString()))
            .thenReturn(List.of());

        // Act
        PageResponse<ProductionSuggestionResponseDTO> result =
            productionSuggestionService.getProductionSuggestions(0, 10, "", "");

        // Assert
        assertNotNull(result);
        assertEquals(0, result.getContent().size());
        assertEquals(0L, result.getTotalElements());
    }

    @Test
    void testGetProductionSuggestionsWithSearch() {
        // Arrange
        when(productionSuggestionRepository.count("Product A")).thenReturn(1L);
        when(productionSuggestionRepository.findAllOrderedByPriority(any(Page.class), eq("Product A"), anyString()))
            .thenReturn(List.of(testSuggestions.get(0)));

        // Act
        PageResponse<ProductionSuggestionResponseDTO> result =
            productionSuggestionService.getProductionSuggestions(0, 10, "Product A", "");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(1L, result.getTotalElements());
    }
}
