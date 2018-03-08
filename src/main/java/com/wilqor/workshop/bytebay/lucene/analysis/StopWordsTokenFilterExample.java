package com.wilqor.workshop.bytebay.lucene.analysis;

import com.wilqor.workshop.bytebay.lucene.config.ConfigLoader;
import com.wilqor.workshop.bytebay.lucene.config.IndexType;
import com.wilqor.workshop.bytebay.lucene.source.Source;
import com.wilqor.workshop.bytebay.lucene.source.model.CommentedReview;
import org.apache.lucene.analysis.Analyzer;

import java.nio.file.Path;

public class StopWordsTokenFilterExample {
    // TODO implement analyzer, which splits tokens by whitespace, puts them in lower case and filters stop words
    // tip: make use of both StandardAnalyzer.STOP_WORDS_SET and custom stop words
    public static class WhitespaceStopWordsFilteringAnalyzer extends Analyzer {
        @Override
        protected TokenStreamComponents createComponents(String fieldName) {
            throw new UnsupportedOperationException();
        }
    }

    public static void main(String[] args) throws Exception {
        Path pathForIndex = ConfigLoader.LOADER.getPathForIndex(IndexType.STOP_WORDS_TOKEN_FILTER_EXAMPLE);
        try (Indexer<CommentedReview> indexer = new WhitespaceTokenizerExample.CommentedReviewIndexer(pathForIndex,
                new WhitespaceStopWordsFilteringAnalyzer())) {
            indexer.index(Source.COMMENTED_MODEL);
        }
    }
}
