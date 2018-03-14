package com.wilqor.workshop.bytebay.lucene.analysis;

import com.wilqor.workshop.bytebay.lucene.indexing.CommentedReviewIndexer;
import com.wilqor.workshop.bytebay.lucene.indexing.Indexer;
import com.wilqor.workshop.bytebay.lucene.source.model.CommentedReview;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.standard.StandardAnalyzer;

import java.io.IOException;
import java.nio.file.Path;

public class StopFilterExample {
    // TODO implement analyzer, which splits tokens by whitespace, puts them in lower case and filters stop words
    // tip 1: token filters typically require tokenizers or other token filters to be constructed
    // tip 2: combine English stop words found in StandardAnalyzer.STOP_WORDS_SET with custom stop words required by tests
    public static class WhitespaceStopWordsFilteringAnalyzer extends Analyzer {
        @Override
        protected TokenStreamComponents createComponents(String fieldName) {
            throw new UnsupportedOperationException("WhitespaceStopWordsFilteringAnalyzer not yet implemented!");
        }
    }

    public static Indexer<CommentedReview> getIndexerForPath(Path pathForIndex) throws IOException {
        return new CommentedReviewIndexer(pathForIndex, new WhitespaceStopWordsFilteringAnalyzer());
    }
}
