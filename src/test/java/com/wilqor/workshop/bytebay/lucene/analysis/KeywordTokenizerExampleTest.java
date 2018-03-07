package com.wilqor.workshop.bytebay.lucene.analysis;

import com.wilqor.workshop.bytebay.lucene.BaseReadingTest;
import com.wilqor.workshop.bytebay.lucene.config.ConfigLoader;
import com.wilqor.workshop.bytebay.lucene.config.IndexType;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.junit.Test;

import java.nio.file.Path;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class KeywordTokenizerExampleTest extends BaseReadingTest {
    private static final int QUERY_MATCHES_LIMIT = 5;

    @Override
    protected Path provideDirectoryPath() {
        return ConfigLoader.LOADER.getPathForIndex(IndexType.KEYWORD_TOKENIZER_EXAMPLE);
    }

    @Test
    public void shouldHaveIndexedAllReviews() throws Exception {
        Query query = new MatchAllDocsQuery();
        TopDocs topDocs = searcher.search(query, QUERY_MATCHES_LIMIT);

        assertThat(topDocs.totalHits, is(5L));
    }

    @Test
    public void shouldRetrieveReviewsByUserName() throws Exception {
        Query query = new TermQuery(new Term(KeywordTokenizerExample.SimpleReviewIndexer.USER_NAME_FIELD, "zbyszkop"));
        TopDocs topDocs = searcher.search(query, QUERY_MATCHES_LIMIT);

        assertThat(topDocs.totalHits, is(3L));
    }

    @Test
    public void shouldRetrieveZeroReviewsForPartialArticleName() throws Exception {
        Query query = new TermQuery(new Term(KeywordTokenizerExample.SimpleReviewIndexer.ARTICLE_NAME_FIELD, "lucene"));
        TopDocs topDocs = searcher.search(query, QUERY_MATCHES_LIMIT);

        assertThat(topDocs.totalHits, is(0L));
    }

    @Test
    public void shouldRetrieveZeroReviewsForArticleNameWithDifferentCase() throws Exception {
        Query query = new TermQuery(new Term(KeywordTokenizerExample.SimpleReviewIndexer.ARTICLE_NAME_FIELD, "lucene 101"));
        TopDocs topDocs = searcher.search(query, QUERY_MATCHES_LIMIT);

        assertThat(topDocs.totalHits, is(0L));
    }

    @Test
    public void shouldRetrieveReviewsForArticleNameWithMatchingCase() throws Exception {
        Query query = new TermQuery(new Term(KeywordTokenizerExample.SimpleReviewIndexer.ARTICLE_NAME_FIELD, "Lucene 101"));
        TopDocs topDocs = searcher.search(query, QUERY_MATCHES_LIMIT);

        assertThat(topDocs.totalHits, is(2L));
    }
}