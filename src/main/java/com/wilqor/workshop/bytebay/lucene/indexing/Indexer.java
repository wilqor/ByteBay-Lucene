package com.wilqor.workshop.bytebay.lucene.indexing;

import com.wilqor.workshop.bytebay.lucene.source.Source;

public interface Indexer<T> extends AutoCloseable {
    void index(Source<T> source);
}
