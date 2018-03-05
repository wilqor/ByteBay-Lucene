package com.wilqor.workshop.bytebay.lucene.wikipediasearch;

import java.util.List;

public interface WikipediaSearcher {

    List<SearchResultEntry> search(String searchString);

    void indexWikipedia();
}
