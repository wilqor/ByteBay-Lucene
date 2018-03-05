package com.wilqor.workshop.bytebay.lucene.wikipediasearch;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
public class SearchService {

    @Autowired
    WikipediaSearcher wikipediaSearcher;

    @PostConstruct
    public void indexContent() {
        wikipediaSearcher.indexWikipedia();
    }

    @RequestMapping(method = GET, value = "/search")
    public SearchResult search(String searchString) {
        long start = System.currentTimeMillis();
        List<SearchResultEntry> searchResults = wikipediaSearcher.search(searchString);
        return SearchResult.builder()
                .searchResults(searchResults)
                .executionTimeMs(System.currentTimeMillis() - start)
                .build();
    }

}
