package com.example.repository;

import com.example.entity.RawMaterial;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

@ApplicationScoped
public class RawMaterialRepository implements PanacheRepository<RawMaterial> {

    public Optional<RawMaterial> findByIdOptional(Long id) {
        return find("id", id).firstResultOptional();
    }

    public boolean existsById(Long id) {
        return count("id", id) > 0;
    }

    public Optional<RawMaterial> findByName(String name) {
        return find("LOWER(name)", name.toLowerCase()).firstResultOptional();
    }

    public boolean existsByName(String name) {
        return count("LOWER(name)", name.toLowerCase()) > 0;
    }

    public boolean existsByNameAndIdNot(String name, Long id) {
        return count("LOWER(name) = ?1 AND id != ?2", name.toLowerCase(), id) > 0;
    }
}
