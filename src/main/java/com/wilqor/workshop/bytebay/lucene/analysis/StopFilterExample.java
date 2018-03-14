package com.wilqor.workshop.bytebay.lucene.analysis;

import com.wilqor.workshop.bytebay.lucene.indexing.CommentedReviewIndexer;
import com.wilqor.workshop.bytebay.lucene.indexing.Indexer;
import com.wilqor.workshop.bytebay.lucene.source.model.CommentedReview;
import org.apache.lucene.analysis.*;
import org.apache.lucene.analysis.core.WhitespaceTokenizer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;

public class StopFilterExample {
    // TODO implement analyzer, which splits tokens by whitespace, puts them in lower case and filters stop words
    // tip 1: token filters typically require tokenizers or other token filters to be constructed
    // tip 2: combine English stop words found in StandardAnalyzer.STOP_WORDS_SET with custom stop words required by tests
    public static class WhitespaceStopWordsFilteringAnalyzer extends Analyzer {
        @Override
        protected TokenStreamComponents createComponents(String fieldName) {
            Tokenizer tokenizer = new WhitespaceTokenizer();
            TokenFilter lowerCaseFilter = new LowerCaseFilter(tokenizer);
            CharArraySet stopWords = CharArraySet.copy(StandardAnalyzer.STOP_WORDS_SET);
            stopWords.addAll(Arrays.asList("i", "nie", "w"));
            TokenFilter stopFilter = new StopFilter(lowerCaseFilter, stopWords);
            return new TokenStreamComponents(tokenizer, stopFilter);
        }
    }

    public static Indexer<CommentedReview> getIndexerForPath(Path pathForIndex) throws IOException {
        return new CommentedReviewIndexer(pathForIndex, new WhitespaceStopWordsFilteringAnalyzer());
    }
}
