package com.wilqor.workshop.bytebay.lucene.analysis;

import com.wilqor.workshop.bytebay.lucene.config.ConfigLoader;
import com.wilqor.workshop.bytebay.lucene.config.IndexType;
import com.wilqor.workshop.bytebay.lucene.source.Source;
import com.wilqor.workshop.bytebay.lucene.source.model.CommentedReview;
import org.apache.lucene.analysis.morfologik.MorfologikAnalyzer;

import java.nio.file.Path;

public class PolishTokenFilterExample {

    public static void main(String[] args) throws Exception {
        MorfologikAnalyzer analyzer = new MorfologikAnalyzer();

        Path pathForIndex = ConfigLoader.LOADER.getPathForIndex(IndexType.POLISH_ANALYZER_EXAMPLE);
        try (Indexer<CommentedReview> indexer = new WhitespaceTokenizerExample.CommentedReviewIndexer(pathForIndex,
                analyzer)) {
            indexer.index(Source.COMMENTED_MODEL);
        }
    }
}
