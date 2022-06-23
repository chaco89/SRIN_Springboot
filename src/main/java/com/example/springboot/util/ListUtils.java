package com.example.springboot.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public final class ListUtils {
    private ListUtils() {}

    public static <T, U> List<U> map(Collection<T> coll, Function<T, U> mapper) {
        if (coll.isEmpty()) {
            return Collections.emptyList();
        }

        return Collections.unmodifiableList(addAll(new ArrayList<>(coll.size()), coll, mapper));
    }

    public static <T, U> List<U> addAll(List<U> list, Collection<T> coll, Function<T, U> mapper) {
        for (T t : coll) {
            list.add(mapper.apply(t));
        }

        return list;
    }

}
