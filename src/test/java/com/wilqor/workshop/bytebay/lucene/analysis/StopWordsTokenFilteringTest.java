package com.wilqor.workshop.bytebay.lucene.analysis;

import com.wilqor.workshop.bytebay.lucene.FSDirectoryReadingTest;
import com.wilqor.workshop.bytebay.lucene.config.ConfigLoader;
import com.wilqor.workshop.bytebay.lucene.config.IndexType;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.junit.Test;

import java.nio.file.Path;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class StopWordsTokenFilteringTest extends FSDirectoryReadingTest {
    private static final int QUERY_MATCHES_LIMIT = 3;

    @Override
    protected Path provideDirectoryPath() {
        return ConfigLoader.LOADER.getPathForIndex(IndexType.STOP_WORDS_TOKEN_FILTER_EXAMPLE);
    }

    @Test
    public void shouldRetrieveZeroEntriesForStopWordsTermsInComment() throws Exception {
        Query query = new BooleanQuery.Builder()
                .add(new TermQuery(new Term(WhitespaceAnalysisExample.CommentedReviewIndexer.COMMENT_FIELD, "nie")), BooleanClause.Occur.SHOULD)
                .add(new TermQuery(new Term(WhitespaceAnalysisExample.CommentedReviewIndexer.COMMENT_FIELD, "i")), BooleanClause.Occur.SHOULD)
                .add(new TermQuery(new Term(WhitespaceAnalysisExample.CommentedReviewIndexer.COMMENT_FIELD, "w")), BooleanClause.Occur.SHOULD)
                .build();
        TopDocs topDocs = searcher.search(query, QUERY_MATCHES_LIMIT);


        assertThat(topDocs.totalHits, is(0L));
    }

    @Test
    public void shouldRetrieveEntriesForNonStopWordTermInComment() throws Exception {
        Query query = new TermQuery(new Term(WhitespaceAnalysisExample.CommentedReviewIndexer.COMMENT_FIELD, "czad"));
        TopDocs topDocs = searcher.search(query, QUERY_MATCHES_LIMIT);


        assertThat(topDocs.totalHits, is(1L));
    }
}