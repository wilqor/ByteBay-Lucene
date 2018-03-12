package com.wilqor.workshop.bytebay.lucene.analysis;

import com.wilqor.workshop.bytebay.lucene.indexing.CommentedReviewIndexer;
import com.wilqor.workshop.bytebay.lucene.indexing.Indexer;
import com.wilqor.workshop.bytebay.lucene.source.model.CommentedReview;
import org.apache.lucene.analysis.Analyzer;

import java.io.IOException;
import java.nio.file.Path;

public class WhitespaceTokenizerExample {
    // TODO implement analyzer, using tokenizer which splits tokens by whitespace
    private static class WhitespaceTokenizingAnalyzer extends Analyzer {

        @Override
        protected TokenStreamComponents createComponents(String fieldName) {
            throw new UnsupportedOperationException();
        }
    }

    public static Indexer<CommentedReview> getIndexerForPath(Path pathForIndex) throws IOException {
        return new CommentedReviewIndexer(pathForIndex, new WhitespaceTokenizingAnalyzer());
    }
}
