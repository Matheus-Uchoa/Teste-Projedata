package com.example.repository;

import com.example.entity.Product;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

@ApplicationScoped
public class ProductRepository implements PanacheRepository<Product> {

    public Optional<Product> findByIdOptional(Long id) {
        return find("id", id).firstResultOptional();
    }

    public boolean existsById(Long id) {
        return count("id", id) > 0;
    }

    public boolean existsByName(String name) {
        return count("LOWER(name)", name.toLowerCase()) > 0;
    }

    public boolean existsByNameAndIdNot(String name, Long id) {
        return count("LOWER(name) = ?1 AND id != ?2", name.toLowerCase(), id) > 0;
    }
}
