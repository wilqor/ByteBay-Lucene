package com.wilqor.workshop.bytebay.lucene.analysis;

import com.wilqor.workshop.bytebay.lucene.config.ConfigLoader;
import com.wilqor.workshop.bytebay.lucene.config.IndexType;
import com.wilqor.workshop.bytebay.lucene.source.Source;
import com.wilqor.workshop.bytebay.lucene.source.model.CommentedReview;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.standard.StandardTokenizer;

import java.nio.file.Path;

public class StandardAnalysisExample {
    private static class StandardTokenizingAnalyzer extends Analyzer {
        @Override
        protected TokenStreamComponents createComponents(String fieldName) {
            Tokenizer tokenizer = new StandardTokenizer();
            return new TokenStreamComponents(tokenizer);
        }
    }

    public static void main(String[] args) throws Exception {
        Path pathForIndex = ConfigLoader.LOADER.getPathForIndex(IndexType.STANDARD_ANALYZER_EXAMPLE);
        try (Indexer<CommentedReview> indexer = new WhitespaceAnalysisExample.CommentedReviewIndexer(pathForIndex,
                new StandardTokenizingAnalyzer())) {
            indexer.index(Source.COMMENTED_MODEL);
        }
    }
}
