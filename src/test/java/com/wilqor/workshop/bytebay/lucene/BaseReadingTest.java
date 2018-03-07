package com.wilqor.workshop.bytebay.lucene;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.IOUtils;
import org.junit.After;
import org.junit.Before;

import java.io.IOException;
import java.nio.file.Path;

public abstract class BaseReadingTest {
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
}
