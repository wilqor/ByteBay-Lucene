package com.wilqor.workshop.bytebay.lucene.wikipediasearch;

import lombok.Builder;
import lombok.Getter;

import java.util.List;


@Getter
@Builder
public class SearchResult {
    private List<SearchResultEntry> searchResults;
    private long executionTimeMs;

}
