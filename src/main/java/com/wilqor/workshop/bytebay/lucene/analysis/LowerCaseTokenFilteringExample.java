package com.wilqor.workshop.bytebay.lucene.analysis;

import com.wilqor.workshop.bytebay.lucene.config.ConfigLoader;
import com.wilqor.workshop.bytebay.lucene.config.IndexType;
import com.wilqor.workshop.bytebay.lucene.source.Source;
import com.wilqor.workshop.bytebay.lucene.source.model.CommentedReview;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.WhitespaceTokenizer;

import java.nio.file.Path;

public class LowerCaseTokenFilteringExample {
    public static class LowerCaseWhitespaceAnalyzer extends Analyzer {
        @Override
        protected TokenStreamComponents createComponents(String fieldName) {
            Tokenizer tokenizer = new WhitespaceTokenizer();
            TokenFilter tokenFilter = new LowerCaseFilter(tokenizer);
            return new TokenStreamComponents(tokenizer, tokenFilter);
        }
    }

    public static void main(String[] args) throws Exception {
        Path pathForIndex = ConfigLoader.LOADER.getPathForIndex(IndexType.LOWER_CASE_TOKEN_FILTER_EXAMPLE);
        try (Indexer<CommentedReview> indexer = new WhitespaceAnalysisExample.CommentedReviewIndexer(pathForIndex,
                new LowerCaseWhitespaceAnalyzer())) {
            indexer.index(Source.COMMENTED_MODEL);
        }
    }
}
