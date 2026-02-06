package com.example.repository;

import com.example.entity.ProductionSuggestion;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class ProductionSuggestionRepository implements PanacheRepository<ProductionSuggestion> {

    public List<ProductionSuggestion> findAllOrderedByPriority(Page page, String searchName, String sortDirection) {
        StringBuilder query = new StringBuilder("1=1");
        Map<String, Object> params = new HashMap<>();

        if (searchName != null && !searchName.trim().isEmpty()) {
            query.append(" AND LOWER(productName) LIKE LOWER(:searchName)");
            params.put("searchName", "%" + searchName + "%");
        }

        String orderBy = " ORDER BY ";
        if ("asc".equalsIgnoreCase(sortDirection) || "desc".equalsIgnoreCase(sortDirection)) {
            orderBy += "productValue " + sortDirection.toUpperCase() + ", priorityRank ASC";
        } else {
            orderBy += "priorityRank ASC";
        }

        return find(query.toString() + orderBy, params)
                .page(page)
                .list();
    }

    public long count(String searchName) {
        if (searchName != null && !searchName.trim().isEmpty()) {
            return count("LOWER(productName) LIKE LOWER(?1)", "%" + searchName + "%");
        }
        return count();
    }
}
