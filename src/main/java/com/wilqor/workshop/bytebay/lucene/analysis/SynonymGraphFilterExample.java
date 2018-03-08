package com.wilqor.workshop.bytebay.lucene.analysis;

import com.wilqor.workshop.bytebay.lucene.config.ConfigLoader;
import com.wilqor.workshop.bytebay.lucene.config.IndexType;
import com.wilqor.workshop.bytebay.lucene.source.Source;
import com.wilqor.workshop.bytebay.lucene.source.model.CommentedReview;
import org.apache.lucene.analysis.Analyzer;

import java.nio.file.Path;

public class SynonymGraphFilterExample {
    // TODO implement analyzer, which performs standard tokenization, puts tokens in lower case and filters test case synonyms
    // tip: result of synonym graph filtering needs to be flattened with FlattenGraphFilter
    public static class SynonymGraphFilteringAnalyzer extends Analyzer {
        @Override
        protected TokenStreamComponents createComponents(String fieldName) {
            throw new UnsupportedOperationException();
        }
    }

    public static void main(String[] args) throws Exception {
        Path pathForIndex = ConfigLoader.LOADER.getPathForIndex(IndexType.SYNONYM_GRAPH_FILTER_EXAMPLE);
        try (Indexer<CommentedReview> indexer = new WhitespaceTokenizerExample.CommentedReviewIndexer(pathForIndex,
                new SynonymGraphFilteringAnalyzer())) {
            indexer.index(Source.COMMENTED_MODEL);
        }
    }
}
