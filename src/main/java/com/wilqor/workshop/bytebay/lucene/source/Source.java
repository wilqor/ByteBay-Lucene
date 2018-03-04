package com.wilqor.workshop.bytebay.lucene.source;

import java.util.stream.Stream;

public interface Source<T> {
    Source<SimpleReview> SIMPLE_MODEL = new JsonSource<>("simple_model.json.batch", SimpleReview.class);
    Source<CommentedReview> COMMENTED_MODEL = new JsonSource<>("commented_model.json.batch", CommentedReview.class);

    Stream<T> stream();
}
