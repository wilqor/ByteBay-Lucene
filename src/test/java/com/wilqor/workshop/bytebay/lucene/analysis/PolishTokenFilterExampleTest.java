package com.wilqor.workshop.bytebay.lucene.analysis;

import com.wilqor.workshop.bytebay.lucene.BaseReadingTest;
import com.wilqor.workshop.bytebay.lucene.config.ConfigLoader;
import com.wilqor.workshop.bytebay.lucene.config.IndexType;
import org.apache.lucene.analysis.morfologik.MorfologikAnalyzer;
import org.apache.lucene.queryparser.flexible.standard.StandardQueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;

import static com.wilqor.workshop.bytebay.lucene.source.model.CommentedReview.COMMENT_FIELD;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class PolishTokenFilterExampleTest extends BaseReadingTest
{

    @Override
    protected Path provideDirectoryPath() throws IOException {
        return ConfigLoader.LOADER.getPathForIndex(IndexType.POLISH_ANALYZER_EXAMPLE);
    }

    @Test
    public void shouldFindWordInADifferentForm() throws Exception {
        StandardQueryParser parser = new StandardQueryParser(new MorfologikAnalyzer());
        Query query = parser.parse("polecany", "comment");
        TopDocs search = searcher.search(query, 10);

        assertThat(search.totalHits, is(2L));

        Arrays.stream(search.scoreDocs)
                .map(scoreDoc -> getField(scoreDoc.doc, COMMENT_FIELD))
                .forEach(comment -> System.out.println("Comment: " + comment));

    }


    private String getField(int docId, String field) throws RuntimeException {
        try {
            return searcher.doc(docId).getField(field).stringValue();
        } catch (IOException e) {
            throw new RuntimeException(e);

        }
    }
}