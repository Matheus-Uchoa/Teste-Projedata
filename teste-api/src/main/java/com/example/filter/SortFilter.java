package com.example.filter;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

public class SortFilter<T, R extends Comparable<R>> {
    private final Function<T, R> fieldExtractor;
    private final String direction;

    public SortFilter(Function<T, R> fieldExtractor, String direction) {
        this.fieldExtractor = fieldExtractor;
        this.direction = direction;
    }

    public void apply(List<T> list) {
        Comparator<T> comparator = Comparator.comparing(fieldExtractor);
        if ("desc".equalsIgnoreCase(direction)) {
            comparator = comparator.reversed();
        }
        list.sort(comparator);
    }

    public static <T, R extends Comparable<R>> SortFilter<T, R> by(Function<T, R> fieldExtractor, String direction) {
        return new SortFilter<>(fieldExtractor, direction);
    }
}
