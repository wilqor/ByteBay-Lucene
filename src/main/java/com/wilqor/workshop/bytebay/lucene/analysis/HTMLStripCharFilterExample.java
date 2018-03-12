package com.wilqor.workshop.bytebay.lucene.analysis;

import com.wilqor.workshop.bytebay.lucene.config.ConfigLoader;
import com.wilqor.workshop.bytebay.lucene.config.IndexType;
import com.wilqor.workshop.bytebay.lucene.indexing.CommentedReviewIndexer;
import com.wilqor.workshop.bytebay.lucene.indexing.Indexer;
import com.wilqor.workshop.bytebay.lucene.source.Source;
import com.wilqor.workshop.bytebay.lucene.source.model.CommentedReview;
import org.apache.lucene.analysis.Analyzer;

import java.nio.file.Path;

public class HTMLStripCharFilterExample {
    // TODO implement analyzer, which applies HTML characters char filter and performs standard tokenization
    private static class HTMLStrippingAnalyzer extends Analyzer {
        @Override
        protected TokenStreamComponents createComponents(String fieldName) {
            throw new UnsupportedOperationException();
        }

    }

    public static void main(String[] args) throws Exception {
        Path pathForIndex = ConfigLoader.LOADER.getPathForIndex(IndexType.HTML_CHAR_STRIP_FILTER_EXAMPLE);
        try (Indexer<CommentedReview> indexer = new CommentedReviewIndexer(pathForIndex,
                new HTMLStrippingAnalyzer())) {
            indexer.index(Source.COMMENTED_HTML_MODEL);
        }
    }
}
