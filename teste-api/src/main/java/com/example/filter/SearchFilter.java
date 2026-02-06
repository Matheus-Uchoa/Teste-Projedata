package com.example.filter;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

public class SearchFilter<T> implements QueryFilter<T> {
    private final PanacheRepository<T> repository;
    private final String fieldName;
    private final String searchValue;

    public SearchFilter(PanacheRepository<T> repository, String fieldName, String searchValue) {
        this.repository = repository;
        this.fieldName = fieldName;
        this.searchValue = searchValue;
    }

    @Override
    public PanacheQuery<T> apply(PanacheQuery<T> query) {
        if (searchValue != null && !searchValue.trim().isEmpty()) {
            String queryStr = String.format("LOWER(%s) LIKE LOWER(?1)", fieldName);
            return repository.find(queryStr, "%" + searchValue + "%");
        }
        return query;
    }

    public static <T> SearchFilter<T> byField(PanacheRepository<T> repository, String fieldName, String searchValue) {
        return new SearchFilter<>(repository, fieldName, searchValue);
    }
}
