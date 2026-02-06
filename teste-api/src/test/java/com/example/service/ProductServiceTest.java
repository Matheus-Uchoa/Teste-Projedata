package com.example.service;

import com.example.dto.PageResponse;
import com.example.dto.product.ProductRequestDTO;
import com.example.dto.product.ProductResponseDTO;
import com.example.entity.Product;
import com.example.exception.ResourceNotFoundException;
import com.example.repository.ProductRepository;
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
class ProductServiceTest {

    @Inject
    ProductService productService;

    @InjectMock
    ProductRepository productRepository;

    @InjectMock
    com.example.repository.ProductRawMaterialRepository productRawMaterialRepository;

    private Product testProduct;
    private ProductRequestDTO testProductRequest;

    @BeforeEach
    void setUp() {
        Mockito.reset(productRepository);
        Mockito.reset(productRawMaterialRepository);

        testProduct = new Product();
        testProduct.setId(1L);
        testProduct.setName("Test Product");
        testProduct.setValue(new BigDecimal("100.00"));

        testProductRequest = new ProductRequestDTO("Test Product", new BigDecimal("100.00"));
    }

    @Test
    void testFindAll() {
        // Arrange
        PanacheQuery<Product> mockQuery = mock(PanacheQuery.class);
        when(productRepository.findAll()).thenReturn(mockQuery);
        when(mockQuery.count()).thenReturn(1L);
        when(mockQuery.page(any(Page.class))).thenReturn(mockQuery);
        when(mockQuery.list()).thenReturn(List.of(testProduct));

        // Act
        PageResponse<ProductResponseDTO> result = productService.findAll(0, 10, null, null, null);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals("Test Product", result.getContent().get(0).name());
        assertEquals(0, result.getPageNumber());
        assertEquals(10, result.getPageSize());
        assertEquals(1L, result.getTotalElements());
    }

    @Test
    void testFindAllWithSearch() {
        // Arrange
        PanacheQuery<Product> mockQuery = mock(PanacheQuery.class);
        when(productRepository.find(anyString(), (Object[]) any())).thenReturn(mockQuery);
        when(mockQuery.count()).thenReturn(1L);
        when(mockQuery.page(any(Page.class))).thenReturn(mockQuery);
        when(mockQuery.list()).thenReturn(List.of(testProduct));

        // Act
        PageResponse<ProductResponseDTO> result = productService.findAll(0, 10, "test", null, null);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        verify(productRepository).find(anyString(), (Object[]) any());
    }

    @Test
    void testFindById() {
        // Arrange
        when(productRepository.findByIdOptional(1L)).thenReturn(Optional.of(testProduct));

        // Act
        ProductResponseDTO result = productService.findById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("Test Product", result.name());
        assertEquals(new BigDecimal("100.00"), result.value());
    }

    @Test
    void testFindByIdNotFound() {
        // Arrange
        when(productRepository.findByIdOptional(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> productService.findById(999L));
    }

    @Test
    void testCreate() {
        // Arrange
        Product newProduct = new Product();
        newProduct.setName("Test Product");
        newProduct.setValue(new BigDecimal("100.00"));

        doAnswer(invocation -> {
            Product product = invocation.getArgument(0);
            product.setId(1L);
            return null;
        }).when(productRepository).persist(any(Product.class));

        // Act
        ProductResponseDTO result = productService.create(testProductRequest);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("Test Product", result.name());
        verify(productRepository).persist(any(Product.class));
    }

    @Test
    void testUpdate() {
        // Arrange
        when(productRepository.findByIdOptional(1L)).thenReturn(Optional.of(testProduct));
        ProductRequestDTO updateRequest = new ProductRequestDTO("Updated Product", new BigDecimal("200.00"));

        // Act
        ProductResponseDTO result = productService.update(1L, updateRequest);

        // Assert
        assertNotNull(result);
        assertEquals("Updated Product", result.name());
        assertEquals(new BigDecimal("200.00"), result.value());
    }

    @Test
    void testUpdateNotFound() {
        // Arrange
        when(productRepository.findByIdOptional(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class,
            () -> productService.update(999L, testProductRequest));
    }

    @Test
    void testDelete() {
        // Arrange
        when(productRepository.findByIdOptional(1L)).thenReturn(Optional.of(testProduct));
        when(productRawMaterialRepository.hasProductLinks(1L)).thenReturn(false);
        doNothing().when(productRepository).delete(testProduct);

        // Act
        productService.delete(1L);

        // Assert
        verify(productRawMaterialRepository).hasProductLinks(1L);
        verify(productRepository).delete(testProduct);
    }

    @Test
    void testDeleteNotFound() {
        // Arrange
        when(productRepository.findByIdOptional(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> productService.delete(999L));
    }

    @Test
    void testDeleteWithLinkedMaterials() {
        // Arrange
        when(productRepository.findByIdOptional(1L)).thenReturn(Optional.of(testProduct));
        when(productRawMaterialRepository.hasProductLinks(1L)).thenReturn(true);

        // Act & Assert
        assertThrows(com.example.exception.ReferentialIntegrityException.class,
            () -> productService.delete(1L));
        verify(productRawMaterialRepository).hasProductLinks(1L);
        verify(productRepository, never()).delete(any());
    }
}
