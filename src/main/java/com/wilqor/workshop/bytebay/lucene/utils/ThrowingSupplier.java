package com.wilqor.workshop.bytebay.lucene.utils;

import java.util.function.Supplier;

@FunctionalInterface
public interface ThrowingSupplier<T> extends Supplier<T> {
    @Override
    default T get() {
        try {
            return throwingGet();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    T throwingGet() throws Exception;
}
