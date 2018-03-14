package com.wilqor.workshop.bytebay.lucene.source;

import com.wilqor.workshop.bytebay.lucene.source.model.CommentedReview;
import com.wilqor.workshop.bytebay.lucene.source.model.SimpleReview;
import com.wilqor.workshop.bytebay.lucene.source.model.WikipediaPage;

import java.util.stream.Stream;

public interface Source<T> {
    Source<SimpleReview> SIMPLE_MODEL = new JsonSource<>("simple_model.json.batch", SimpleReview.class);
    Source<CommentedReview> COMMENTED_MODEL = new JsonSource<>("commented_model.json.batch", CommentedReview.class);
    // to use full Wikipedia pages archive, change name of file obtained from src/main/resources
    Source<WikipediaPage> WIKIPEDIA_PAGE_MODEL = new WikipediaSource("plwiki_head.json.batch.bz2");

    Stream<T> stream();
}
