package com.wilqor.workshop.bytebay.lucene.analysis;

import com.wilqor.workshop.bytebay.lucene.BaseReadingTest;
import com.wilqor.workshop.bytebay.lucene.config.ConfigLoader;
import com.wilqor.workshop.bytebay.lucene.config.IndexType;
import com.wilqor.workshop.bytebay.lucene.indexing.Indexer;
import com.wilqor.workshop.bytebay.lucene.source.Source;
import com.wilqor.workshop.bytebay.lucene.source.model.CommentedReview;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Path;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class SynonymGraphFilterExampleTest extends BaseReadingTest {
    private static final int QUERY_MATCHES_LIMIT = 3;

    @Override
    public void setUp() throws Exception {
        try (Indexer<CommentedReview> indexer = SynonymGraphFilterExample.getIndexerForPath(provideDirectoryPath())) {
            indexer.index(Source.COMMENTED_MODEL);
        }
        super.setUp();
    }

    @Override
    @Before
    protected Path provideDirectoryPath() {
        return ConfigLoader.LOADER.getPathForIndex(IndexType.SYNONYM_GRAPH_FILTER_EXAMPLE);
    }

    @Test
    public void shouldRetrieveEntriesForOriginalCommentTerm() throws Exception {
        Query query = new TermQuery(new Term(CommentedReview.COMMENT_FIELD, "czad"));
        TopDocs topDocs = searcher.search(query, QUERY_MATCHES_LIMIT);

        assertThat(topDocs.totalHits, is(1L));
    }

    @Test
    public void shouldRetrieveEntriesForSynonymOneCommentTerm() throws Exception {
        Query query = new TermQuery(new Term(CommentedReview.COMMENT_FIELD, "bomba"));
        TopDocs topDocs = searcher.search(query, QUERY_MATCHES_LIMIT);

        assertThat(topDocs.totalHits, is(1L));
    }

    @Test
    public void shouldRetrieveEntriesForSynonymTwoCommentTerm() throws Exception {
        Query query = new TermQuery(new Term(CommentedReview.COMMENT_FIELD, "super"));
        TopDocs topDocs = searcher.search(query, QUERY_MATCHES_LIMIT);

        assertThat(topDocs.totalHits, is(1L));
    }

    @Test
    public void shouldRetrieveEntriesForSynonymThreeCommentTerm() throws Exception {
        Query query = new TermQuery(new Term(CommentedReview.COMMENT_FIELD, "mega"));
        TopDocs topDocs = searcher.search(query, QUERY_MATCHES_LIMIT);

        assertThat(topDocs.totalHits, is(1L));
    }
}