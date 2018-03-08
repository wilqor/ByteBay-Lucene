package com.wilqor.workshop.bytebay.lucene.analysis;

import com.wilqor.workshop.bytebay.lucene.config.ConfigLoader;
import com.wilqor.workshop.bytebay.lucene.config.IndexType;
import com.wilqor.workshop.bytebay.lucene.source.Source;
import com.wilqor.workshop.bytebay.lucene.source.model.CommentedReview;
import org.apache.lucene.analysis.Analyzer;

import java.nio.file.Path;

public class WhitespaceTokenizerExample {
    // TODO implement indexer, which indexes user name and thumb as string, index article name and comment as text
    public static class CommentedReviewIndexer implements Indexer<CommentedReview> {

        CommentedReviewIndexer(Path directoryPath, Analyzer analyzer) {

        }

        @Override
        public void index(Source<CommentedReview> source) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void close() throws Exception {
            throw new UnsupportedOperationException();
        }
    }

    // TODO implement analyzer, using tokenizer which splits tokens by whitespace
    private static class WhitespaceTokenizingAnalyzer extends Analyzer {

        @Override
        protected TokenStreamComponents createComponents(String fieldName) {
            throw new UnsupportedOperationException();
        }
    }

    public static void main(String[] args) throws Exception {
        Path pathForIndex = ConfigLoader.LOADER.getPathForIndex(IndexType.WHITESPACE_TOKENIZER_EXAMPLE);
        try (Indexer<CommentedReview> indexer = new CommentedReviewIndexer(pathForIndex,
                new WhitespaceTokenizingAnalyzer())) {
            indexer.index(Source.COMMENTED_MODEL);
        }

    }
}
