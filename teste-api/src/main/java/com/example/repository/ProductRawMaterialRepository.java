package com.example.repository;

import com.example.entity.ProductRawMaterial;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class ProductRawMaterialRepository implements PanacheRepository<ProductRawMaterial> {

    public io.quarkus.hibernate.orm.panache.PanacheQuery<ProductRawMaterial> findByProductId(Long productId) {
        return find("product.id", productId);
    }

    public long countByProductId(Long productId) {
        return count("product.id", productId);
    }

    public Optional<ProductRawMaterial> findByProductIdAndRawMaterialId(Long productId, Long rawMaterialId) {
        return find("product.id = ?1 and rawMaterial.id = ?2", productId, rawMaterialId).firstResultOptional();
    }

    public boolean existsByProductIdAndRawMaterialId(Long productId, Long rawMaterialId) {
        return count("product.id = ?1 and rawMaterial.id = ?2", productId, rawMaterialId) > 0;
    }

    public void deleteByProductIdAndRawMaterialId(Long productId, Long rawMaterialId) {
        delete("product.id = ?1 and rawMaterial.id = ?2", productId, rawMaterialId);
    }

    public boolean hasProductLinks(Long productId) {
        return count("product.id", productId) > 0;
    }

    public boolean hasRawMaterialLinks(Long rawMaterialId) {
        return count("rawMaterial.id", rawMaterialId) > 0;
    }
}
