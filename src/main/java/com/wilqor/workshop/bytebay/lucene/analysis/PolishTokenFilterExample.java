package com.wilqor.workshop.bytebay.lucene.analysis;

import com.wilqor.workshop.bytebay.lucene.indexing.CommentedReviewIndexer;
import com.wilqor.workshop.bytebay.lucene.indexing.Indexer;
import com.wilqor.workshop.bytebay.lucene.source.model.CommentedReview;
import org.apache.lucene.analysis.morfologik.MorfologikAnalyzer;

import java.io.IOException;
import java.nio.file.Path;

public class PolishTokenFilterExample {
    public static Indexer<CommentedReview> getIndexerForPath(Path pathForIndex) throws IOException {
        return new CommentedReviewIndexer(pathForIndex, new MorfologikAnalyzer());
    }
}
