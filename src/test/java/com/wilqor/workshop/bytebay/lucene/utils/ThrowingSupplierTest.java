package com.wilqor.workshop.bytebay.lucene.utils;

import org.junit.Test;

import java.io.IOException;

public class ThrowingSupplierTest {
    @Test(expected = RuntimeException.class)
    public void shouldWrapCheckedException() {
        ThrowingSupplier<String> throwingSupplier = () -> {
            throw new IOException("some IO Exception");
        };
        throwingSupplier.get();
    }
}