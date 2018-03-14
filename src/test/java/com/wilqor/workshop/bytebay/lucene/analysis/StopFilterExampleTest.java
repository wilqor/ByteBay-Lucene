package com.wilqor.workshop.bytebay.lucene.analysis;

import com.wilqor.workshop.bytebay.lucene.BaseReadingTest;
import com.wilqor.workshop.bytebay.lucene.config.ConfigLoader;
import com.wilqor.workshop.bytebay.lucene.config.IndexType;
import com.wilqor.workshop.bytebay.lucene.indexing.Indexer;
import com.wilqor.workshop.bytebay.lucene.source.Source;
import com.wilqor.workshop.bytebay.lucene.source.model.CommentedReview;
import com.wilqor.workshop.bytebay.lucene.source.model.SimpleReview;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.junit.Test;

import java.nio.file.Path;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class StopFilterExampleTest extends BaseReadingTest {
    private static final int QUERY_MATCHES_LIMIT = 3;

    @Override
    public void setUp() throws Exception {
        try (Indexer<CommentedReview> indexer = StopFilterExample.getIndexerForPath(provideDirectoryPath())) {
            indexer.index(Source.COMMENTED_MODEL);
        }
        super.setUp();
    }

    @Override
    protected Path provideDirectoryPath() {
        return ConfigLoader.LOADER.getPathForIndex(IndexType.STOP_FILTER_EXAMPLE);
    }

    @Test
    public void shouldRetrieveZeroReviewsForFullArticleName() throws Exception {
        Query query = new TermQuery(new Term(SimpleReview.ARTICLE_NAME_FIELD, "Lucene 101"));
        TopDocs topDocs = searcher.search(query, QUERY_MATCHES_LIMIT);

        assertThat(topDocs.totalHits, is(0L));
    }

    @Test
    public void shouldRetrieveZeroReviewsForArticleNameTermWithInUpperCase() throws Exception {
        Query query = new TermQuery(new Term(SimpleReview.ARTICLE_NAME_FIELD, "Lucene"));
        TopDocs topDocs = searcher.search(query, QUERY_MATCHES_LIMIT);

        assertThat(topDocs.totalHits, is(0L));
    }

    @Test
    public void shouldRetrieveReviewsForArticleNameTermInLowerCase() throws Exception {
        Query query = new TermQuery(new Term(SimpleReview.ARTICLE_NAME_FIELD, "lucene"));
        TopDocs topDocs = searcher.search(query, QUERY_MATCHES_LIMIT);

        assertThat(topDocs.totalHits, is(5L));
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
    public void shouldRetrieveZeroEntriesForStopWordInArticleName() throws Exception {
        Query query = new TermQuery(new Term(CommentedReview.ARTICLE_NAME_FIELD, "in"));
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