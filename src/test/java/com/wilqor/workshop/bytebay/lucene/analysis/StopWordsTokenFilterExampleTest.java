package com.wilqor.workshop.bytebay.lucene.analysis;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.nio.file.Path;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.junit.Test;

import com.wilqor.workshop.bytebay.lucene.BaseReadingTest;
import com.wilqor.workshop.bytebay.lucene.config.ConfigLoader;
import com.wilqor.workshop.bytebay.lucene.config.IndexType;
import com.wilqor.workshop.bytebay.lucene.source.model.CommentedReview;

public class StopWordsTokenFilterExampleTest extends BaseReadingTest {
    private static final int QUERY_MATCHES_LIMIT = 3;

    @Override
    protected Path provideDirectoryPath() {
        return ConfigLoader.LOADER.getPathForIndex(IndexType.STOP_WORDS_TOKEN_FILTER_EXAMPLE);
    }

    @Test
    public void shouldRetrieveZeroEntriesForStopWordsTermsInComment() throws Exception {
        Query query = new BooleanQuery.Builder()
                .add(new TermQuery(new Term(CommentedReview.COMMENT_FIELD, "nie")), BooleanClause.Occur.SHOULD)
                .add(new TermQuery(new Term(CommentedReview.COMMENT_FIELD, "i")), BooleanClause.Occur.SHOULD)
                .add(new TermQuery(new Term(CommentedReview.COMMENT_FIELD, "w")), BooleanClause.Occur.SHOULD)
                .build();
        TopDocs topDocs = searcher.search(query, QUERY_MATCHES_LIMIT);


        assertThat(topDocs.totalHits, is(0L));
    }

    @Test
    public void shouldRetrieveEntriesForNonStopWordTermInComment() throws Exception {
        Query query = new TermQuery(new Term(CommentedReview.COMMENT_FIELD, "czad"));
        TopDocs topDocs = searcher.search(query, QUERY_MATCHES_LIMIT);


        assertThat(topDocs.totalHits, is(1L));
    }
}