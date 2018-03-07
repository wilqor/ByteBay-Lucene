package com.wilqor.workshop.bytebay.lucene.source;

import com.wilqor.workshop.bytebay.lucene.source.model.JsonWikipediaPage;
import com.wilqor.workshop.bytebay.lucene.source.model.WikipediaPage;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class WikipediaSource implements Source<WikipediaPage> {


    private final JsonSource<JsonWikipediaPage> jsonSource;

    public WikipediaSource(String fileName) {
        jsonSource = new JsonSource<>(fileName, JsonWikipediaPage.class);
    }

    @Override
    public Stream<WikipediaPage> stream() {
        return jsonSource
                .stream()
                .map(WikipediaPage::fromJsonWikipediaPage);
    }


    public static void main(String[] args) {
        List<WikipediaPage> reviews = Source.WIKIPEDIA_PAGE_MODEL.stream().limit(10).collect(Collectors.toList());

        reviews.forEach(System.out::println);
    }
}
