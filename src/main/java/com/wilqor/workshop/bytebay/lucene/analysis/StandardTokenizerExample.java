package com.wilqor.workshop.bytebay.lucene.analysis;

import com.wilqor.workshop.bytebay.lucene.config.ConfigLoader;
import com.wilqor.workshop.bytebay.lucene.config.IndexType;
import com.wilqor.workshop.bytebay.lucene.indexing.CommentedReviewIndexer;
import com.wilqor.workshop.bytebay.lucene.indexing.Indexer;
import com.wilqor.workshop.bytebay.lucene.source.Source;
import com.wilqor.workshop.bytebay.lucene.source.model.CommentedReview;
import org.apache.lucene.analysis.Analyzer;

import java.nio.file.Path;

public class StandardTokenizerExample {
    // TODO implement analyzer, which performs only standard tokenization
    private static class StandardTokenizingAnalyzer extends Analyzer {
        @Override
        protected TokenStreamComponents createComponents(String fieldName) {
            throw new UnsupportedOperationException();
        }
    }

    public static void main(String[] args) throws Exception {
        Path pathForIndex = ConfigLoader.LOADER.getPathForIndex(IndexType.STANDARD_TOKENIZER_EXAMPLE);
        try (Indexer<CommentedReview> indexer = new CommentedReviewIndexer(pathForIndex,
                new StandardTokenizingAnalyzer())) {
            indexer.index(Source.COMMENTED_MODEL);
        }
    }
}
