package com.wilqor.workshop.bytebay.lucene.analysis;

import com.wilqor.workshop.bytebay.lucene.source.Source;
import com.wilqor.workshop.bytebay.lucene.source.model.JsonWikipediaPage;

public class WikipediaIndexer implements Indexer<JsonWikipediaPage> {

    @Override
    public void index(Source<JsonWikipediaPage> source) {
//        source.stream()
//                .map()

    }



    @Override
    public void close() throws Exception {

    }
}
