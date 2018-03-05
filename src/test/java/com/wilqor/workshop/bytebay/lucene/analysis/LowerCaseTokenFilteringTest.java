package com.wilqor.workshop.bytebay.lucene.analysis;

import com.wilqor.workshop.bytebay.lucene.FSDirectoryReadingTest;
import com.wilqor.workshop.bytebay.lucene.config.ConfigLoader;
import com.wilqor.workshop.bytebay.lucene.config.IndexType;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.junit.Test;

import java.nio.file.Path;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class LowerCaseTokenFilteringTest extends FSDirectoryReadingTest {
    private static final int QUERY_MATCHES_LIMIT = 3;

    @Override
    protected Path provideDirectoryPath() {
        return ConfigLoader.LOADER.getPathForIndex(IndexType.LOWER_CASE_TOKEN_FILTER_EXAMPLE);
    }

    @Test
    public void shouldRetrieveZeroReviewsForFullArticleName() throws Exception {
        Query query = new TermQuery(new Term(WhitespaceAnalysisExample.CommentedReviewIndexer.ARTICLE_NAME_FIELD, "Lucene 101"));
        TopDocs topDocs = searcher.search(query, QUERY_MATCHES_LIMIT);

        assertThat(topDocs.totalHits, is(0L));
    }

    @Test
    public void shouldRetrieveZeroReviewsForArticleNameTermWithInUpperCase() throws Exception {
        Query query = new TermQuery(new Term(WhitespaceAnalysisExample.CommentedReviewIndexer.ARTICLE_NAME_FIELD, "Lucene"));
        TopDocs topDocs = searcher.search(query, QUERY_MATCHES_LIMIT);

        assertThat(topDocs.totalHits, is(0L));
    }

    @Test
    public void shouldRetrieveReviewsForArticleNameTermInLowerCase() throws Exception {
        Query query = new TermQuery(new Term(WhitespaceAnalysisExample.CommentedReviewIndexer.ARTICLE_NAME_FIELD, "lucene"));
        TopDocs topDocs = searcher.search(query, QUERY_MATCHES_LIMIT);

        assertThat(topDocs.totalHits, is(5L));
    }
}
