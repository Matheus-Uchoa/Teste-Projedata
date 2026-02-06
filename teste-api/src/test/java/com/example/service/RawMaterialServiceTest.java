package com.example.service;

import com.example.dto.PageResponse;
import com.example.dto.rawmaterial.RawMaterialRequestDTO;
import com.example.dto.rawmaterial.RawMaterialResponseDTO;
import com.example.entity.RawMaterial;
import com.example.exception.ResourceNotFoundException;
import com.example.repository.RawMaterialRepository;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.InjectMock;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@QuarkusTest
class RawMaterialServiceTest {

    @Inject
    RawMaterialService rawMaterialService;

    @InjectMock
    RawMaterialRepository rawMaterialRepository;

    @InjectMock
    com.example.repository.ProductRawMaterialRepository productRawMaterialRepository;

    private RawMaterial testRawMaterial;
    private RawMaterialRequestDTO testRawMaterialRequest;

    @BeforeEach
    void setUp() {
        Mockito.reset(rawMaterialRepository);
        Mockito.reset(productRawMaterialRepository);

        testRawMaterial = new RawMaterial();
        testRawMaterial.setId(1L);
        testRawMaterial.setName("Test Material");
        testRawMaterial.setStockQuantity(new BigDecimal("50.00"));

        testRawMaterialRequest = new RawMaterialRequestDTO("Test Material", new BigDecimal("50.00"));
    }

    @Test
    void testFindAll() {
        // Arrange
        PanacheQuery<RawMaterial> mockQuery = mock(PanacheQuery.class);
        when(rawMaterialRepository.findAll()).thenReturn(mockQuery);
        when(mockQuery.count()).thenReturn(1L);
        when(mockQuery.page(any(Page.class))).thenReturn(mockQuery);
        when(mockQuery.list()).thenReturn(List.of(testRawMaterial));

        // Act
        PageResponse<RawMaterialResponseDTO> result = rawMaterialService.findAll(0, 10, null, null, null);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals("Test Material", result.getContent().get(0).name());
        assertEquals(new BigDecimal("50.00"), result.getContent().get(0).stockQuantity());
    }

    @Test
    void testFindById() {
        // Arrange
        when(rawMaterialRepository.findByIdOptional(1L)).thenReturn(Optional.of(testRawMaterial));

        // Act
        RawMaterialResponseDTO result = rawMaterialService.findById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("Test Material", result.name());
        assertEquals(new BigDecimal("50.00"), result.stockQuantity());
    }

    @Test
    void testFindByIdNotFound() {
        // Arrange
        when(rawMaterialRepository.findByIdOptional(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> rawMaterialService.findById(999L));
    }

    @Test
    void testCreate() {
        // Arrange
        doAnswer(invocation -> {
            RawMaterial material = invocation.getArgument(0);
            material.setId(1L);
            return null;
        }).when(rawMaterialRepository).persist(any(RawMaterial.class));

        // Act
        RawMaterialResponseDTO result = rawMaterialService.create(testRawMaterialRequest);

        // Assert
        assertNotNull(result);
        assertEquals("Test Material", result.name());
        verify(rawMaterialRepository).persist(any(RawMaterial.class));
    }

    @Test
    void testUpdate() {
        // Arrange
        when(rawMaterialRepository.findByIdOptional(1L)).thenReturn(Optional.of(testRawMaterial));
        RawMaterialRequestDTO updateRequest = new RawMaterialRequestDTO("Updated Material", new BigDecimal("100.00"));

        // Act
        RawMaterialResponseDTO result = rawMaterialService.update(1L, updateRequest);

        // Assert
        assertNotNull(result);
        assertEquals("Updated Material", result.name());
        assertEquals(new BigDecimal("100.00"), result.stockQuantity());
    }

    @Test
    void testUpdateNotFound() {
        // Arrange
        when(rawMaterialRepository.findByIdOptional(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class,
            () -> rawMaterialService.update(999L, testRawMaterialRequest));
    }

    @Test
    void testDelete() {
        // Arrange
        when(rawMaterialRepository.findByIdOptional(1L)).thenReturn(Optional.of(testRawMaterial));
        when(productRawMaterialRepository.hasRawMaterialLinks(1L)).thenReturn(false);
        doNothing().when(rawMaterialRepository).delete(testRawMaterial);

        // Act
        rawMaterialService.delete(1L);

        // Assert
        verify(productRawMaterialRepository).hasRawMaterialLinks(1L);
        verify(rawMaterialRepository).delete(testRawMaterial);
    }

    @Test
    void testDeleteNotFound() {
        // Arrange
        when(rawMaterialRepository.findByIdOptional(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> rawMaterialService.delete(999L));
    }

    @Test
    void testDeleteWithLinkedProducts() {
        // Arrange
        when(rawMaterialRepository.findByIdOptional(1L)).thenReturn(Optional.of(testRawMaterial));
        when(productRawMaterialRepository.hasRawMaterialLinks(1L)).thenReturn(true);

        // Act & Assert
        assertThrows(com.example.exception.ReferentialIntegrityException.class,
            () -> rawMaterialService.delete(1L));
        verify(productRawMaterialRepository).hasRawMaterialLinks(1L);
        verify(rawMaterialRepository, never()).delete(any());
    }
}
