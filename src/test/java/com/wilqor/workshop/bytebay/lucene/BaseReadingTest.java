package com.wilqor.workshop.bytebay.lucene;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.IOUtils;
import org.junit.After;
import org.junit.Before;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;

public abstract class BaseReadingTest {
    protected static final Logger LOGGER = LogManager.getLogger(BaseReadingTest.class);

    protected IndexSearcher searcher;
    private Directory directory;
    private IndexReader reader;

    @Before
    public void setUp() throws Exception {
        directory = FSDirectory.open(provideDirectoryPath());
        reader = DirectoryReader.open(directory);
        searcher = new IndexSearcher(reader);
    }

    @After
    public void tearDown() throws Exception {
        IOUtils.close(reader, directory);
    }

    protected abstract Path provideDirectoryPath() throws IOException;

    protected void explainQueryResult(Query query, TopDocs topDocs) {
        Arrays.stream(topDocs.scoreDocs).forEach(scoreDoc -> {
            try {
                Explanation explain = searcher.explain(query, scoreDoc.doc);
                LOGGER.info("Explain for document {} is:\n{}", scoreDoc.doc, explain);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
