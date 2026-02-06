package com.example.mapper;

import com.example.dto.rawmaterial.RawMaterialRequestDTO;
import com.example.dto.rawmaterial.RawMaterialResponseDTO;
import com.example.entity.RawMaterial;

public class RawMaterialMapper {

    public static RawMaterial toEntity(RawMaterialRequestDTO dto) {
        RawMaterial rawMaterial = new RawMaterial();
        rawMaterial.setName(dto.name());
        rawMaterial.setStockQuantity(dto.stockQuantity());
        return rawMaterial;
    }

    public static RawMaterialResponseDTO toResponseDTO(RawMaterial rawMaterial) {
        return new RawMaterialResponseDTO(
                rawMaterial.getId(),
                rawMaterial.getName(),
                rawMaterial.getStockQuantity()
        );
    }

    public static void updateEntityFromDTO(RawMaterial rawMaterial, RawMaterialRequestDTO dto) {
        rawMaterial.setName(dto.name());
        rawMaterial.setStockQuantity(dto.stockQuantity());
    }
}
