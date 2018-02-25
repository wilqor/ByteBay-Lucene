package com.wilqor.workshop.bytebay.lucene;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Path;

public abstract class FSDirectoryReadingTest extends BaseReadingTest {
    @Override
    protected Directory provideDirectory() throws IOException {
        return FSDirectory.open(provideDirectoryPath());
    }

    protected abstract Path provideDirectoryPath() throws IOException;
}
