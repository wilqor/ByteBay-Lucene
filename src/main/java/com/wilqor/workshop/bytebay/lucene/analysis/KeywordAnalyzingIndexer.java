package com.wilqor.workshop.bytebay.lucene.analysis;

import com.wilqor.workshop.bytebay.lucene.config.ConfigLoader;
import com.wilqor.workshop.bytebay.lucene.config.IndexType;
import com.wilqor.workshop.bytebay.lucene.source.SimpleReview;
import com.wilqor.workshop.bytebay.lucene.source.Source;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.IOUtils;

import java.io.IOException;
import java.nio.file.Path;

public class KeywordAnalyzingIndexer implements Indexer<SimpleReview> {
    private static final Logger LOGGER = LogManager.getLogger(ConfigLoader.class);

    static final String USER_NAME_FIELD = "user_name";
    static final String THUMB_FIELD = "thumb";
    static final String ARTICLE_NAME_FIELD = "article_name";

    private final FSDirectory directory;
    private final Analyzer analyzer;
    private final IndexWriter indexWriter;

    KeywordAnalyzingIndexer(Path targetDirectory) throws IOException {
        directory = FSDirectory.open(targetDirectory);
        LOGGER.info("Opened index in directory: {}", targetDirectory);
        analyzer = new KeywordAnalyzer();
        indexWriter = new IndexWriter(directory, new IndexWriterConfig(analyzer));
    }


    @Override
    public void index(Source<SimpleReview> source) {
        source.stream().forEach(simpleReview -> {
            Document reviewDocument = mapToDocument(simpleReview);
            try {
                indexWriter.addDocument(reviewDocument);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private Document mapToDocument(SimpleReview simpleReview) {
        Document reviewDocument = new Document();
        reviewDocument.add(new StringField(USER_NAME_FIELD, simpleReview.getUserName(), Field.Store.YES));
        reviewDocument.add(new StringField(THUMB_FIELD, simpleReview.getThumb().name(), Field.Store.YES));
        reviewDocument.add(new StringField(ARTICLE_NAME_FIELD, simpleReview.getArticleName(), Field.Store.YES));
        return reviewDocument;
    }

    @Override
    public void close() throws Exception {
        Path directoryPath = directory.getDirectory();
        IOUtils.close(indexWriter, directory, analyzer);
        LOGGER.info("Closed index in directory: {}", directoryPath);
    }

    public static void main(String[] args) throws Exception {
        try (Indexer<SimpleReview> indexer = new KeywordAnalyzingIndexer(ConfigLoader.LOADER.getPathForIndex(IndexType.KEYWORD_ANALYZER_EXAMPLE))) {
            indexer.index(Source.SIMPLE_MODEL);
        }
    }
}
