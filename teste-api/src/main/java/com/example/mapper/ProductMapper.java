package com.example.mapper;

import com.example.dto.product.ProductRequestDTO;
import com.example.dto.product.ProductResponseDTO;
import com.example.entity.Product;

public class ProductMapper {

    public static Product toEntity(ProductRequestDTO dto) {
        Product product = new Product();
        product.setName(dto.name());
        product.setValue(dto.value());
        return product;
    }

    public static ProductResponseDTO toResponseDTO(Product product) {
        return new ProductResponseDTO(
                product.getId(),
                product.getName(),
                product.getValue()
        );
    }

    public static void updateEntityFromDTO(Product product, ProductRequestDTO dto) {
        product.setName(dto.name());
        product.setValue(dto.value());
    }
}
