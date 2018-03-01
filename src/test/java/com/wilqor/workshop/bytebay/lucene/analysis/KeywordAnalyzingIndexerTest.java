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

public class KeywordAnalyzingIndexerTest extends FSDirectoryReadingTest {

    @Override
    protected Path provideDirectoryPath() {
        return ConfigLoader.LOADER.getPathForIndex(IndexType.KEYWORD_ANALYZER_EXAMPLE);
    }

    @Test
    public void shouldRetrieveThreeCommentsByZbyszko() throws Exception {
        Query query = new TermQuery(new Term(KeywordAnalyzingIndexer.USER_NAME_FIELD, "zbyszkop"));
        TopDocs topDocs = searcher.search(query, 10);

        assertThat(topDocs.totalHits, is(3L));
    }
}