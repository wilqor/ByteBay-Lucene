package com.wilqor.workshop.bytebay.lucene.analysis;

import com.wilqor.workshop.bytebay.lucene.config.ConfigLoader;
import com.wilqor.workshop.bytebay.lucene.config.IndexType;
import com.wilqor.workshop.bytebay.lucene.source.Source;
import com.wilqor.workshop.bytebay.lucene.source.model.CommentedReview;
import org.apache.lucene.analysis.*;
import org.apache.lucene.analysis.core.WhitespaceTokenizer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;

import java.nio.file.Path;

public class StopWordsTokenFilterExample {
    public static class WhitespaceStopWordsFilteringAnalyzer extends Analyzer {
        @Override
        protected TokenStreamComponents createComponents(String fieldName) {
            Tokenizer tokenizer = new WhitespaceTokenizer();
            CharArraySet stopSet = CharArraySet.copy(StandardAnalyzer.STOP_WORDS_SET);
            stopSet.add("i");
            stopSet.add("nie");
            stopSet.add("w");
            TokenFilter filter = new LowerCaseFilter(tokenizer);
            filter = new StopFilter(filter, stopSet);
            return new TokenStreamComponents(tokenizer, filter);
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
