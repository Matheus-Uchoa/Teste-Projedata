package com.example.filter;

import io.quarkus.hibernate.orm.panache.PanacheQuery;

public interface QueryFilter<T> {
    PanacheQuery<T> apply(PanacheQuery<T> query);
}
