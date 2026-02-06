package com.example.service;

import com.example.dto.PageResponse;
import com.example.dto.product.ProductRequestDTO;
import com.example.dto.product.ProductResponseDTO;
import com.example.entity.Product;
import com.example.exception.DuplicateResourceException;
import com.example.exception.ReferentialIntegrityException;
import com.example.exception.ResourceNotFoundException;
import com.example.filter.PaginationFilter;
import com.example.filter.SearchFilter;
import com.example.filter.SortFilter;
import com.example.mapper.ProductMapper;
import com.example.repository.ProductRepository;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class ProductService {

    @Inject
    ProductRepository productRepository;

    @Inject
    com.example.repository.ProductRawMaterialRepository productRawMaterialRepository;

    public PageResponse<ProductResponseDTO> findAll(int pageNumber, int pageSize, String search, String sortBy, String sortDirection) {
        PanacheQuery<Product> query = productRepository.findAll();

        SearchFilter<Product> searchFilter = SearchFilter.byField(productRepository, "name", search);
        query = searchFilter.apply(query);

        long totalElements = query.count();

        PaginationFilter<Product> paginationFilter = PaginationFilter.of(pageNumber, pageSize);
        query = paginationFilter.apply(query);

        List<Product> products = query.list();

        if ("value".equals(sortBy) && sortDirection != null && !sortDirection.isEmpty()) {
            SortFilter.by(Product::getValue, sortDirection).apply(products);
        }

        List<ProductResponseDTO> content = products.stream()
                .map(ProductMapper::toResponseDTO)
                .collect(Collectors.toList());

        return new PageResponse<>(content, pageNumber, pageSize, totalElements);
    }

    public ProductResponseDTO findById(Long id) {
        Product product = productRepository.findByIdOptional(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", id));
        return ProductMapper.toResponseDTO(product);
    }

    @Transactional
    public ProductResponseDTO create(@Valid ProductRequestDTO dto) {
        if (productRepository.existsByName(dto.name())) {
            throw new DuplicateResourceException("Product", "name", dto.name());
        }
        Product product = ProductMapper.toEntity(dto);
        productRepository.persist(product);
        return ProductMapper.toResponseDTO(product);
    }

    @Transactional
    public ProductResponseDTO update(Long id, @Valid ProductRequestDTO dto) {
        Product product = productRepository.findByIdOptional(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", id));

        if (productRepository.existsByNameAndIdNot(dto.name(), id)) {
            throw new DuplicateResourceException("Product", "name", dto.name());
        }

        ProductMapper.updateEntityFromDTO(product, dto);
        return ProductMapper.toResponseDTO(product);
    }

    @Transactional
    public void delete(Long id) {
        Product product = productRepository.findByIdOptional(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", id));

        if (productRawMaterialRepository.hasProductLinks(id)) {
            throw new ReferentialIntegrityException("Cannot delete product because it has linked raw materials");
        }

        productRepository.delete(product);
    }
}
