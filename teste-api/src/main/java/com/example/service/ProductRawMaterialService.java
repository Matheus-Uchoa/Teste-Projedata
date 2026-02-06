package com.example.service;

import com.example.dto.PageResponse;
import com.example.dto.productrawmaterial.ProductRawMaterialRequestDTO;
import com.example.dto.productrawmaterial.ProductRawMaterialResponseDTO;
import com.example.entity.Product;
import com.example.entity.ProductRawMaterial;
import com.example.entity.RawMaterial;
import com.example.exception.DuplicateResourceException;
import com.example.exception.ResourceNotFoundException;
import com.example.mapper.ProductRawMaterialMapper;
import com.example.repository.ProductRawMaterialRepository;
import com.example.repository.ProductRepository;
import com.example.repository.RawMaterialRepository;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class ProductRawMaterialService {

    @Inject
    ProductRawMaterialRepository productRawMaterialRepository;

    @Inject
    ProductRepository productRepository;

    @Inject
    RawMaterialRepository rawMaterialRepository;

    public PageResponse<ProductRawMaterialResponseDTO> findByProductId(Long productId, int pageNumber, int pageSize) {
        // Verify product exists
        productRepository.findByIdOptional(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", productId));

        long totalElements = productRawMaterialRepository.countByProductId(productId);
        List<ProductRawMaterialResponseDTO> content = productRawMaterialRepository.findByProductId(productId)
                .page(Page.of(pageNumber, pageSize))
                .stream()
                .map(ProductRawMaterialMapper::toResponseDTO)
                .collect(Collectors.toList());

        return new PageResponse<>(content, pageNumber, pageSize, totalElements);
    }

    public ProductRawMaterialResponseDTO findByProductIdAndRawMaterialId(Long productId, Long rawMaterialId) {
        ProductRawMaterial entity = productRawMaterialRepository
                .findByProductIdAndRawMaterialId(productId, rawMaterialId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Association between Product %d and Raw Material %d not found", productId, rawMaterialId)));

        return ProductRawMaterialMapper.toResponseDTO(entity);
    }

    @Transactional
    public ProductRawMaterialResponseDTO addRawMaterialToProduct(Long productId, @Valid ProductRawMaterialRequestDTO dto) {
        // Verify product exists
        Product product = productRepository.findByIdOptional(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", productId));

        // Verify raw material exists
        RawMaterial rawMaterial = rawMaterialRepository.findByIdOptional(dto.rawMaterialId())
                .orElseThrow(() -> new ResourceNotFoundException("Raw Material", dto.rawMaterialId()));

        // Check if association already exists
        if (productRawMaterialRepository.existsByProductIdAndRawMaterialId(productId, dto.rawMaterialId())) {
            throw new DuplicateResourceException(
                    String.format("Raw Material '%s' is already associated with Product '%s'", 
                            rawMaterial.getName(), product.getName()));
        }

        // Create association
        ProductRawMaterial entity = new ProductRawMaterial(product, rawMaterial, dto.quantityNeeded());
        productRawMaterialRepository.persist(entity);

        return ProductRawMaterialMapper.toResponseDTO(entity);
    }

    @Transactional
    public ProductRawMaterialResponseDTO updateQuantity(Long productId, Long rawMaterialId, @Valid ProductRawMaterialRequestDTO dto) {
        ProductRawMaterial entity = productRawMaterialRepository
                .findByProductIdAndRawMaterialId(productId, rawMaterialId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Association between Product %d and Raw Material %d not found", productId, rawMaterialId)));

        entity.setQuantityNeeded(dto.quantityNeeded());

        return ProductRawMaterialMapper.toResponseDTO(entity);
    }

    @Transactional
    public void removeRawMaterialFromProduct(Long productId, Long rawMaterialId) {
        // Verify association exists
        if (!productRawMaterialRepository.existsByProductIdAndRawMaterialId(productId, rawMaterialId)) {
            throw new ResourceNotFoundException(
                    String.format("Association between Product %d and Raw Material %d not found", productId, rawMaterialId));
        }

        productRawMaterialRepository.deleteByProductIdAndRawMaterialId(productId, rawMaterialId);
    }
}
