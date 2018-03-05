package com.wilqor.workshop.bytebay.lucene.analysis;

import com.wilqor.workshop.bytebay.lucene.config.ConfigLoader;
import com.wilqor.workshop.bytebay.lucene.config.IndexType;
import com.wilqor.workshop.bytebay.lucene.source.model.CommentedReview;
import com.wilqor.workshop.bytebay.lucene.source.Source;
import com.wilqor.workshop.bytebay.lucene.utils.ThrowingSupplier;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;

import java.nio.file.Path;

public class WhitespaceAnalysisExample {
    public static void main(String[] args) throws Exception {
        Path pathForIndex = ConfigLoader.LOADER.getPathForIndex(IndexType.WHITESPACE_ANALYZER_EXAMPLE);
        ThrowingSupplier<Indexer<CommentedReview>> supplier = () -> new CommentedReviewIndexer(pathForIndex, new WhitespaceAnalyzer());
        IndexerRunner.of(pathForIndex, supplier, Source.COMMENTED_MODEL).runIndexer();
    }
}
