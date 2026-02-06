package com.example.filter;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;

public class PaginationFilter<T> implements QueryFilter<T> {
    private final int pageNumber;
    private final int pageSize;

    public PaginationFilter(int pageNumber, int pageSize) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
    }

    @Override
    public PanacheQuery<T> apply(PanacheQuery<T> query) {
        return query.page(Page.of(pageNumber, pageSize));
    }

    public static <T> PaginationFilter<T> of(int pageNumber, int pageSize) {
        return new PaginationFilter<>(pageNumber, pageSize);
    }
}
