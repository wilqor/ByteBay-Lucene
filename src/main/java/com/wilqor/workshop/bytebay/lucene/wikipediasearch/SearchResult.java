package com.wilqor.workshop.bytebay.lucene.wikipediasearch;

import java.util.List;

public class SearchResult {
    private List<SearchResultEntry> searchResults;
    private long executionTimeMs;

    public List<SearchResultEntry> getSearchResults() {
        return searchResults;
    }

    public long getExecutionTimeMs() {
        return executionTimeMs;
    }


    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private List<SearchResultEntry> searchResults;
        private long executionTimeMs;

        private Builder() {
        }

        public Builder searchResults(List<SearchResultEntry> searchResults) {
            this.searchResults = searchResults;
            return this;
        }

        public Builder executionTimeMs(long executionTimeMs) {
            this.executionTimeMs = executionTimeMs;
            return this;
        }

        public SearchResult build() {
            SearchResult searchResult = new SearchResult();
            searchResult.executionTimeMs = this.executionTimeMs;
            searchResult.searchResults = this.searchResults;
            return searchResult;
        }
    }
}
