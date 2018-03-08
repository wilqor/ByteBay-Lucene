package com.wilqor.workshop.bytebay.lucene.analysis;

import com.wilqor.workshop.bytebay.lucene.config.ConfigLoader;
import com.wilqor.workshop.bytebay.lucene.config.IndexType;
import com.wilqor.workshop.bytebay.lucene.source.Source;
import com.wilqor.workshop.bytebay.lucene.source.model.CommentedReview;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.FlattenGraphFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.synonym.SynonymGraphFilter;
import org.apache.lucene.analysis.synonym.SynonymMap;
import org.apache.lucene.util.CharsRef;
import org.apache.lucene.util.CharsRefBuilder;

import java.io.IOException;
import java.nio.file.Path;

public class SynonymGraphFilterExample {
    public static class SynonymGraphFilteringAnalyzer extends Analyzer {
        @Override
        protected TokenStreamComponents createComponents(String fieldName) {
            Tokenizer tokenizer = new StandardTokenizer();
            TokenFilter filter = new LowerCaseFilter(tokenizer);
            SynonymMap.Builder synonymMapBuilder = new SynonymMap.Builder();
            synonymMapBuilder.add(new CharsRef("czad"),
                    SynonymMap.Builder.join(new String[]{"bomba", "super", "mega"}, new CharsRefBuilder()),
                    true);
            try {
                filter = new SynonymGraphFilter(filter, synonymMapBuilder.build(), true);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            filter = new FlattenGraphFilter(filter);
            return new TokenStreamComponents(tokenizer, filter);
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
