package com.wilqor.workshop.bytebay.lucene;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

import java.io.IOException;

public abstract class RAMDirectoryReadingTest extends BaseReadingTest {
    @Override
    protected Directory provideDirectory() throws IOException {
        Directory directory = new RAMDirectory();
        setupIndex(directory);
        return directory;
    }

    protected abstract void setupIndex(Directory directory) throws IOException;
}
