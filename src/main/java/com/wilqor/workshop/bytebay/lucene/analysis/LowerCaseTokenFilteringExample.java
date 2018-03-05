package com.wilqor.workshop.bytebay.lucene.analysis;

import com.wilqor.workshop.bytebay.lucene.config.ConfigLoader;
import com.wilqor.workshop.bytebay.lucene.config.IndexType;
import com.wilqor.workshop.bytebay.lucene.source.model.CommentedReview;
import com.wilqor.workshop.bytebay.lucene.source.Source;
import com.wilqor.workshop.bytebay.lucene.utils.ThrowingSupplier;
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
        ThrowingSupplier<Indexer<CommentedReview>> supplier = () -> new CommentedReviewIndexer(pathForIndex,
                new LowerCaseWhitespaceAnalyzer());
        IndexerRunner.of(pathForIndex, supplier, Source.COMMENTED_MODEL).runIndexer();
    }
}
