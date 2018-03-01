package com.wilqor.workshop.bytebay.lucene.source;

import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

public class SimpleReviewSourceTest {
    @Test
    public void shouldParseExample() {
        List<SimpleReview> reviews = Source.SIMPLE_MODEL.stream()
                .collect(Collectors.toList());

        assertThat(reviews, hasSize(5));
    }
}
