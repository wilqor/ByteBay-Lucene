package com.wilqor.workshop.bytebay.lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.IntPoint;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class ExampleReadingTest extends RAMDirectoryReadingTest {
    private static final String AGE_FIELD = "age";
    private static final String NAME_FIELD = "name";
    private static final int MAX_MATCHES = 10;

    @Override
    protected void setupIndex(Directory directory) throws IOException {
        Analyzer analyzer = new KeywordAnalyzer();
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
        try (IndexWriter writer = new IndexWriter(directory, indexWriterConfig)) {
            writer.addDocument(makePerson("Steve", 56));
            writer.addDocument(makePerson("Bill", 62));
            writer.addDocument(makePerson("Lucene", 19));
            writer.addDocument(makePerson("Cassandra", 10));
        }
    }

    @Test
    public void shouldFindFourPeople() throws Exception {
        TopDocs topDocs = searcher.search(new MatchAllDocsQuery(), MAX_MATCHES);

        assertThat(topDocs.totalHits, is(4L));
    }

    @Test
    public void shouldFindOneUnderAgePerson() throws Exception {
        int MATURITY_AGE = 18;
        Query underAgeQuery = IntPoint.newRangeQuery(AGE_FIELD, Integer.MIN_VALUE, MATURITY_AGE);
        TopDocs topDocs = searcher.search(underAgeQuery, MAX_MATCHES);

        assertThat(topDocs.totalHits, is(1L));
    }

    private Document makePerson(String name, int age) {
        Document document = new Document();
        document.add(new StringField(NAME_FIELD, name, Field.Store.YES));
        document.add(new IntPoint(AGE_FIELD, age));
        return document;
    }
}
