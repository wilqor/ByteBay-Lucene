package com.wilqor.workshop.bytebay.lucene.analysis;

import com.wilqor.workshop.bytebay.lucene.config.ConfigLoader;
import com.wilqor.workshop.bytebay.lucene.config.IndexType;
import com.wilqor.workshop.bytebay.lucene.source.model.SimpleReview;
import com.wilqor.workshop.bytebay.lucene.source.Source;
import com.wilqor.workshop.bytebay.lucene.utils.ThrowingSupplier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.lucene.analysis.core.KeywordAnalyzer;

import java.nio.file.Path;

public class KeywordAnalysisExample {
    public static void main(String[] args) throws Exception {
        Path pathForIndex = ConfigLoader.LOADER.getPathForIndex(IndexType.KEYWORD_ANALYZER_EXAMPLE);
        ThrowingSupplier<Indexer<SimpleReview>> supplier = () -> new SimpleReviewIndexer(pathForIndex, new KeywordAnalyzer());
        IndexerRunner.of(pathForIndex, supplier, Source.SIMPLE_MODEL).runIndexer();
    }
}
