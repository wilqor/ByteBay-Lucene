package com.wilqor.workshop.bytebay.lucene.wikipediasearch;

import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.Collections.emptyList;

@Component
public class LuceneWikipediaSearcher implements WikipediaSearcher {

    @Override
    public List<SearchResultEntry> search(String searchString) {
        return  emptyList();
    }

    @Override
    public void indexWikipedia() {

    }
}
