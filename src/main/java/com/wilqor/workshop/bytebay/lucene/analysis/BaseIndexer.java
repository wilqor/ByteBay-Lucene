package com.wilqor.workshop.bytebay.lucene.analysis;

import com.wilqor.workshop.bytebay.lucene.source.Source;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.IOUtils;

import java.io.IOException;
import java.nio.file.Path;

public abstract class BaseIndexer<T> implements Indexer<T> {
    private static final Logger LOGGER = LogManager.getLogger(BaseIndexer.class);

    private final FSDirectory directory;
    private final Analyzer analyzer;
    private final IndexWriter indexWriter;

    BaseIndexer(Path targetDirectory, Analyzer analyzer) throws IOException {
        directory = FSDirectory.open(targetDirectory);
        LOGGER.info("Opened index in directory: {}", directory.getDirectory());
        this.analyzer = analyzer;
        indexWriter = new IndexWriter(directory, new IndexWriterConfig(this.analyzer));
    }

    @Override
    public void index(Source<T> source) {
        source.stream().forEach(sourceItem -> {
            Document sourceDocument = mapToDocument(sourceItem);
            try {
                indexWriter.addDocument(sourceDocument);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

    }

    protected abstract Document mapToDocument(T sourceItem);

    @Override
    public void close() throws Exception {
        Path directoryPath = directory.getDirectory();
        IOUtils.close(indexWriter, directory, analyzer);
        LOGGER.info("Closed index in directory: {}", directoryPath);
    }
}
