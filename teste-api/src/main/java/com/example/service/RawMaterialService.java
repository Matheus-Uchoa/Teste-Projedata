package com.example.service;

import com.example.dto.PageResponse;
import com.example.dto.rawmaterial.RawMaterialRequestDTO;
import com.example.dto.rawmaterial.RawMaterialResponseDTO;
import com.example.entity.RawMaterial;
import com.example.exception.DuplicateResourceException;
import com.example.exception.ReferentialIntegrityException;
import com.example.exception.ResourceNotFoundException;
import com.example.filter.PaginationFilter;
import com.example.filter.SearchFilter;
import com.example.filter.SortFilter;
import com.example.mapper.RawMaterialMapper;
import com.example.repository.RawMaterialRepository;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class RawMaterialService {

    @Inject
    RawMaterialRepository rawMaterialRepository;

    @Inject
    com.example.repository.ProductRawMaterialRepository productRawMaterialRepository;

    public PageResponse<RawMaterialResponseDTO> findAll(int pageNumber, int pageSize, String search, String sortBy, String sortDirection) {
        PanacheQuery<RawMaterial> query = rawMaterialRepository.findAll();

        SearchFilter<RawMaterial> searchFilter = SearchFilter.byField(rawMaterialRepository, "name", search);
        query = searchFilter.apply(query);

        long totalElements = query.count();

        PaginationFilter<RawMaterial> paginationFilter = PaginationFilter.of(pageNumber, pageSize);
        query = paginationFilter.apply(query);

        List<RawMaterial> rawMaterials = query.list();

        if ("stockQuantity".equals(sortBy) && sortDirection != null && !sortDirection.isEmpty()) {
            SortFilter.by(RawMaterial::getStockQuantity, sortDirection).apply(rawMaterials);
        }

        List<RawMaterialResponseDTO> content = rawMaterials.stream()
                .map(RawMaterialMapper::toResponseDTO)
                .collect(Collectors.toList());

        return new PageResponse<>(content, pageNumber, pageSize, totalElements);
    }

    public RawMaterialResponseDTO findById(Long id) {
        RawMaterial rawMaterial = rawMaterialRepository.findByIdOptional(id)
                .orElseThrow(() -> new ResourceNotFoundException("Raw Material", id));
        return RawMaterialMapper.toResponseDTO(rawMaterial);
    }

    @Transactional
    public RawMaterialResponseDTO create(@Valid RawMaterialRequestDTO dto) {
        if (rawMaterialRepository.existsByName(dto.name())) {
            throw new DuplicateResourceException("Raw Material", "name", dto.name());
        }
        
        RawMaterial rawMaterial = RawMaterialMapper.toEntity(dto);
        rawMaterialRepository.persist(rawMaterial);
        return RawMaterialMapper.toResponseDTO(rawMaterial);
    }

    @Transactional
    public RawMaterialResponseDTO update(Long id, @Valid RawMaterialRequestDTO dto) {
        RawMaterial rawMaterial = rawMaterialRepository.findByIdOptional(id)
                .orElseThrow(() -> new ResourceNotFoundException("Raw Material", id));
        
        if (rawMaterialRepository.existsByNameAndIdNot(dto.name(), id)) {
            throw new DuplicateResourceException("Raw Material", "name", dto.name());
        }
        
        RawMaterialMapper.updateEntityFromDTO(rawMaterial, dto);
        return RawMaterialMapper.toResponseDTO(rawMaterial);
    }

    @Transactional
    public void delete(Long id) {
        RawMaterial rawMaterial = rawMaterialRepository.findByIdOptional(id)
                .orElseThrow(() -> new ResourceNotFoundException("Raw Material", id));

        if (productRawMaterialRepository.hasRawMaterialLinks(id)) {
            throw new ReferentialIntegrityException("Cannot delete raw material because it is linked to products");
        }

        rawMaterialRepository.delete(rawMaterial);
    }
}
