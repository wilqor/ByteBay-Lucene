package com.wilqor.workshop.bytebay.lucene.analysis;

import com.wilqor.workshop.bytebay.lucene.BaseReadingTest;
import com.wilqor.workshop.bytebay.lucene.config.ConfigLoader;
import com.wilqor.workshop.bytebay.lucene.config.IndexType;
import com.wilqor.workshop.bytebay.lucene.indexing.Indexer;
import com.wilqor.workshop.bytebay.lucene.source.Source;
import com.wilqor.workshop.bytebay.lucene.source.model.CommentedReview;
import org.apache.lucene.analysis.morfologik.MorfologikAnalyzer;
import org.apache.lucene.queryparser.flexible.standard.StandardQueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class PolishTokenFilterExampleTest extends BaseReadingTest {
    @Override
    public void setUp() throws Exception {
        try (Indexer<CommentedReview> indexer = PolishTokenFilterExample.getIndexerForPath(provideDirectoryPath())) {
            indexer.index(Source.COMMENTED_MODEL);
        }
        super.setUp();
    }

    @Override
    protected Path provideDirectoryPath() {
        return ConfigLoader.LOADER.getPathForIndex(IndexType.POLISH_ANALYZER_EXAMPLE);
    }

    @Test
    public void shouldFindWordInADifferentForm() throws Exception {
        StandardQueryParser parser = new StandardQueryParser(new MorfologikAnalyzer());
        Query query = parser.parse("polecany", CommentedReview.COMMENT_FIELD);
        TopDocs search = searcher.search(query, 10);

        assertThat(search.totalHits, is(2L));

        Arrays.stream(search.scoreDocs)
                .map(scoreDoc -> getField(scoreDoc.doc, CommentedReview.COMMENT_FIELD))
                .forEach(comment -> LOGGER.info("Comment: {}", comment));

    }

    private String getField(int docId, String field) throws RuntimeException {
        try {
            return searcher.doc(docId).getField(field).stringValue();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}