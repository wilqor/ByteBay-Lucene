package com.wilqor.workshop.bytebay.lucene.analysis;

import com.wilqor.workshop.bytebay.lucene.indexing.CommentedReviewIndexer;
import com.wilqor.workshop.bytebay.lucene.indexing.Indexer;
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

    public static Indexer<CommentedReview> getIndexerForPath(Path pathForIndex) throws Exception {
        return new CommentedReviewIndexer(pathForIndex, new SynonymGraphFilteringAnalyzer());
    }
}
