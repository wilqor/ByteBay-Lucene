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

public class SynonymTokenFilterExampleTest extends FSDirectoryReadingTest {
    private static final int QUERY_MATCHES_LIMIT = 3;

    @Override
    protected Path provideDirectoryPath() {
        return ConfigLoader.LOADER.getPathForIndex(IndexType.SYNONYM_TOKEN_FILTER_EXAMPLE);
    }

    @Test
    public void shouldRetrieveEntriesForOriginalCommentTerm() throws Exception {
        Query query = new TermQuery(new Term(WhitespaceTokenizerExample.CommentedReviewIndexer.COMMENT_FIELD, "czad"));
        TopDocs topDocs = searcher.search(query, QUERY_MATCHES_LIMIT);

        assertThat(topDocs.totalHits, is(1L));
    }

    @Test
    public void shouldRetrieveEntriesForSynonymOneCommentTerm() throws Exception {
        Query query = new TermQuery(new Term(WhitespaceTokenizerExample.CommentedReviewIndexer.COMMENT_FIELD, "bomba"));
        TopDocs topDocs = searcher.search(query, QUERY_MATCHES_LIMIT);

        assertThat(topDocs.totalHits, is(1L));
    }

    @Test
    public void shouldRetrieveEntriesForSynonymTwoCommentTerm() throws Exception {
        Query query = new TermQuery(new Term(WhitespaceTokenizerExample.CommentedReviewIndexer.COMMENT_FIELD, "super"));
        TopDocs topDocs = searcher.search(query, QUERY_MATCHES_LIMIT);

        assertThat(topDocs.totalHits, is(1L));
    }

    @Test
    public void shouldRetrieveEntriesForSynonymThreeCommentTerm() throws Exception {
        Query query = new TermQuery(new Term(WhitespaceTokenizerExample.CommentedReviewIndexer.COMMENT_FIELD, "mega"));
        TopDocs topDocs = searcher.search(query, QUERY_MATCHES_LIMIT);

        assertThat(topDocs.totalHits, is(1L));
    }
}