package com.wilqor.workshop.bytebay.lucene.analysis;

import com.wilqor.workshop.bytebay.lucene.config.ConfigLoader;
import com.wilqor.workshop.bytebay.lucene.config.IndexType;
import com.wilqor.workshop.bytebay.lucene.source.Source;
import com.wilqor.workshop.bytebay.lucene.source.model.CommentedReview;
import com.wilqor.workshop.bytebay.lucene.utils.ThrowingSupplier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.IOUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

public class WhitespaceAnalysisExample {
    public static class CommentedReviewIndexer implements Indexer<CommentedReview> {
        static final String USER_NAME_FIELD = "user_name";
        static final String THUMB_FIELD = "thumb";
        static final String ARTICLE_NAME_FIELD = "article_name";
        static final String COMMENT_FIELD = "comment";

        private static final Logger LOGGER = LogManager.getLogger(WhitespaceAnalysisExample.class);

        private final FSDirectory directory;
        private final Analyzer analyzer;
        private final IndexWriter indexWriter;

        CommentedReviewIndexer(Path targetDirectory, Analyzer analyzer) throws IOException {
            directory = FSDirectory.open(targetDirectory);
            LOGGER.info("Opened index in directory: {}", directory.getDirectory());
            this.analyzer = analyzer;
            indexWriter = new IndexWriter(directory, new IndexWriterConfig(this.analyzer));
        }

        @Override
        public void index(Source<CommentedReview> source) {
            source.stream().forEach(sourceItem -> {
                Document sourceDocument = mapToDocument(sourceItem);
                try {
                    indexWriter.addDocument(sourceDocument);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        private Document mapToDocument(CommentedReview sourceItem) {
            Document reviewDocument = new Document();
            reviewDocument.add(new StringField(USER_NAME_FIELD, sourceItem.getUserName(), Field.Store.YES));
            reviewDocument.add(new StringField(THUMB_FIELD, sourceItem.getThumb().name(), Field.Store.YES));
            reviewDocument.add(new TextField(ARTICLE_NAME_FIELD, sourceItem.getArticleName(), Field.Store.YES));
            Optional.ofNullable(sourceItem.getComment())
                    .ifPresent(comment -> reviewDocument.add(new TextField(COMMENT_FIELD, comment, Field.Store.YES)));
            return reviewDocument;
        }

        @Override
        public void close() throws Exception {
            Path directoryPath = directory.getDirectory();
            IOUtils.close(indexWriter, directory, analyzer);
            LOGGER.info("Closed index in directory: {}", directoryPath);
        }
    }

    public static void main(String[] args) throws Exception {
        Path pathForIndex = ConfigLoader.LOADER.getPathForIndex(IndexType.WHITESPACE_ANALYZER_EXAMPLE);
        ThrowingSupplier<Indexer<CommentedReview>> supplier = () -> new CommentedReviewIndexer(pathForIndex, new WhitespaceAnalyzer());
        IndexerRunner.of(pathForIndex, supplier, Source.COMMENTED_MODEL).runIndexer();
    }
}
