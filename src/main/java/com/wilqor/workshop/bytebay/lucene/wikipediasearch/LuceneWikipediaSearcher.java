package com.wilqor.workshop.bytebay.lucene.wikipediasearch;

import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.Collections.singletonList;

@Component
public class LuceneWikipediaSearcher implements WikipediaSearcher {

    @Override
    public List<SearchResultEntry> search(String searchString) {
        return singletonList(SearchResultEntry.builder()
                .title("Napisz implementację")
                .description("Uzupełnij implementację klasy LuceneWikipediaSearcher")
                .link("https://lucene.apache.org/core/7_2_1/index.html")
                .build());
    }

    @Override
    public void indexWikipedia() {


    }

    public static void main(String[] args) {
        LuceneWikipediaSearcher searcher = new LuceneWikipediaSearcher();
        searcher.indexWikipedia();
    }
}
