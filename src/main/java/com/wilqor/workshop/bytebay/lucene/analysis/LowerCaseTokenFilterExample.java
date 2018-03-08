package com.wilqor.workshop.bytebay.lucene.analysis;

import com.wilqor.workshop.bytebay.lucene.config.ConfigLoader;
import com.wilqor.workshop.bytebay.lucene.config.IndexType;
import com.wilqor.workshop.bytebay.lucene.source.Source;
import com.wilqor.workshop.bytebay.lucene.source.model.CommentedReview;
import org.apache.lucene.analysis.Analyzer;

import java.nio.file.Path;

public class LowerCaseTokenFilterExample {
    // TODO implement analyzer, splitting tokens by whitespace and putting them in lower case
    public static class WhitespaceLowerCaseFilteringAnalyzer extends Analyzer {
        @Override
        protected TokenStreamComponents createComponents(String fieldName) {
            throw new UnsupportedOperationException();
        }
    }

    public static void main(String[] args) throws Exception {
        Path pathForIndex = ConfigLoader.LOADER.getPathForIndex(IndexType.LOWER_CASE_TOKEN_FILTER_EXAMPLE);
        try (Indexer<CommentedReview> indexer = new WhitespaceTokenizerExample.CommentedReviewIndexer(pathForIndex,
                new WhitespaceLowerCaseFilteringAnalyzer())) {
            indexer.index(Source.COMMENTED_MODEL);
        }
    }
}
