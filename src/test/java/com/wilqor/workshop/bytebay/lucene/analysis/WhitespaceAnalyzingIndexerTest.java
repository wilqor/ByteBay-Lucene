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

public class WhitespaceAnalyzingIndexerTest extends FSDirectoryReadingTest {
    private static final int QUERY_MATCHES_LIMIT = 5;

    @Override
    protected Path provideDirectoryPath() {
        return ConfigLoader.LOADER.getPathForIndex(IndexType.WHITESPACE_ANALYZER_EXAMPLE);
    }

    @Test
    public void shouldRetrieveZeroReviewsForFullArticleName() throws Exception {
        Query query = new TermQuery(new Term(WhitespaceAnalyzingIndexer.ARTICLE_NAME_FIELD, "Lucene 101"));
        TopDocs topDocs = searcher.search(query, QUERY_MATCHES_LIMIT);

        assertThat(topDocs.totalHits, is(0L));
    }

    @Test
    public void shouldRetrieveZeroReviewsForArticleNameTermWithDifferentCase() throws Exception {
        Query query = new TermQuery(new Term(WhitespaceAnalyzingIndexer.ARTICLE_NAME_FIELD, "lucene"));
        TopDocs topDocs = searcher.search(query, QUERY_MATCHES_LIMIT);

        assertThat(topDocs.totalHits, is(0L));
    }

    @Test
    public void shouldRetrieveReviewsForArticleNameTermWithMatchingCase() throws Exception {
        Query query = new TermQuery(new Term(WhitespaceAnalyzingIndexer.ARTICLE_NAME_FIELD, "Lucene"));
        TopDocs topDocs = searcher.search(query, QUERY_MATCHES_LIMIT);

        assertThat(topDocs.totalHits, is(5L));
    }

    @Test
    public void shouldRetrieveReviewsForArticleNameTermsWithMatchingCase() throws Exception {
        Query query = new BooleanQuery.Builder()
                .add(new TermQuery(new Term(WhitespaceAnalyzingIndexer.ARTICLE_NAME_FIELD, "Lucene")), BooleanClause.Occur.MUST)
                .add(new TermQuery(new Term(WhitespaceAnalyzingIndexer.ARTICLE_NAME_FIELD, "101")), BooleanClause.Occur.MUST)
                .build();
        TopDocs topDocs = searcher.search(query, QUERY_MATCHES_LIMIT);

        assertThat(topDocs.totalHits, is(2L));
    }

    @Test
    public void shouldRetrieveReviewsForCommentTerm() throws Exception {
        Query query = new TermQuery(new Term(WhitespaceAnalyzingIndexer.COMMENT_FIELD, "polecam"));
        TopDocs topDocs = searcher.search(query, QUERY_MATCHES_LIMIT);

        assertThat(topDocs.totalHits, is(2L));
    }
}