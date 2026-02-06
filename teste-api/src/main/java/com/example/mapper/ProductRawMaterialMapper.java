package com.example.mapper;

import com.example.dto.productrawmaterial.ProductRawMaterialResponseDTO;
import com.example.entity.ProductRawMaterial;

public class ProductRawMaterialMapper {

    public static ProductRawMaterialResponseDTO toResponseDTO(ProductRawMaterial entity) {
        return new ProductRawMaterialResponseDTO(
                entity.getId(),
                entity.getProduct().getId(),
                entity.getProduct().getName(),
                entity.getRawMaterial().getId(),
                entity.getRawMaterial().getName(),
                entity.getQuantityNeeded()
        );
    }
}
