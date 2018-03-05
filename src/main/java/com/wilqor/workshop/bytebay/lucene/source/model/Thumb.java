package com.wilqor.workshop.bytebay.lucene.source.model;

import com.fasterxml.jackson.annotation.JsonCreator;


public enum Thumb {
    UP, DOWN;

    @JsonCreator
    public static Thumb valueOfIgnoreCase(String value) {
        return Thumb.valueOf(value.toUpperCase());
    }
}
