package com.wilqor.workshop.bytebay.lucene.analysis;

import com.wilqor.workshop.bytebay.lucene.BaseReadingTest;
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

public class HTMLStripCharFilterExampleTest extends BaseReadingTest {
    private static final int QUERY_MATCHES_LIMIT = 3;

    @Override
    protected Path provideDirectoryPath() {
        return ConfigLoader.LOADER.getPathForIndex(IndexType.HTML_CHAR_STRIP_FILTER_EXAMPLE);
    }

    @Test
    public void shouldRetrieveCommentsForTermInsideHTMLTag() throws Exception {
        Query query = new TermQuery(new Term(WhitespaceTokenizerExample.CommentedReviewIndexer.COMMENT_FIELD, "polecam"));
        TopDocs topDocs = searcher.search(query, QUERY_MATCHES_LIMIT);

        assertThat(topDocs.totalHits, is(2L));
    }

    @Test
    public void shouldRetrieveCommentsForTermInsideNestedHTMLTagWithAttribute() throws Exception {
        Query query = new TermQuery(new Term(WhitespaceTokenizerExample.CommentedReviewIndexer.COMMENT_FIELD, "Serdecznie"));
        TopDocs topDocs = searcher.search(query, QUERY_MATCHES_LIMIT);

        assertThat(topDocs.totalHits, is(1L));
    }
}