package com.wilqor.workshop.bytebay.lucene.config;

import org.junit.Test;

import java.nio.file.Path;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

public class ConfigLoaderTest {
    @Test
    public void shouldContainPathForEachIndexType() {
        Stream.of(IndexType.values()).forEach(type -> {
            Path pathForIndex = ConfigLoader.LOADER.getPathForIndex(type);
            assertThat(pathForIndex, is(notNullValue()));
        });
    }
}
