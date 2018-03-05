package com.wilqor.workshop.bytebay.lucene.analysis;

import com.wilqor.workshop.bytebay.lucene.config.ConfigLoader;
import com.wilqor.workshop.bytebay.lucene.config.IndexType;
import com.wilqor.workshop.bytebay.lucene.source.Source;
import com.wilqor.workshop.bytebay.lucene.source.model.SimpleReview;
import com.wilqor.workshop.bytebay.lucene.utils.ThrowingSupplier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.facet.FacetsConfig;
import org.apache.lucene.facet.sortedset.SortedSetDocValuesFacetField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.IOUtils;

import java.io.IOException;
import java.nio.file.Path;

public class FacetingIndexer implements Indexer<SimpleReview> {
    static final String USER_NAME_FIELD = "user_name";
    static final String THUMB_FIELD = "thumb";
    static final String ARTICLE_FIELD = "article_name";

    private static final Logger LOGGER = LogManager.getLogger(FacetingIndexer.class);

    private final FSDirectory directory;
    private final IndexWriter indexWriter;
    private final Analyzer analyzer;

    FacetingIndexer(Path targetDirectory) throws IOException {
        directory = FSDirectory.open(targetDirectory);
        LOGGER.info("Opened index in directory: {}", directory.getDirectory());
        analyzer = new KeywordAnalyzer();
        indexWriter = new IndexWriter(directory, new IndexWriterConfig(analyzer));
    }

    @Override
    public void index(Source<SimpleReview> source) {
        source.stream().forEach(simpleReview -> {
            Document reviewDocument = mapToDocument(simpleReview);
            FacetsConfig facetsConfig = new FacetsConfig();
            try {
                Document facetedDocument = facetsConfig.build(reviewDocument);
                indexWriter.addDocument(facetedDocument);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private Document mapToDocument(SimpleReview simpleReview) {
        Document reviewDocument = new Document();
        reviewDocument.add(new SortedSetDocValuesFacetField(USER_NAME_FIELD, simpleReview.getUserName()));
        reviewDocument.add(new SortedSetDocValuesFacetField(THUMB_FIELD, simpleReview.getThumb().name()));
        reviewDocument.add(new StringField(ARTICLE_FIELD, simpleReview.getArticleName(), Field.Store.YES));
        reviewDocument.add(new SortedSetDocValuesFacetField(ARTICLE_FIELD, simpleReview.getArticleName()));
        return reviewDocument;
    }

    @Override
    public void close() throws Exception {
        Path directoryPath = directory.getDirectory();
        IOUtils.close(indexWriter, directory, analyzer);
        LOGGER.info("Closed index in directory: {}", directoryPath);
    }

    public static void main(String[] args) throws Exception {
        Path pathForIndex = ConfigLoader.LOADER.getPathForIndex(IndexType.FACETING_EXAMPLE);
        ThrowingSupplier<Indexer<SimpleReview>> supplier = () -> new FacetingIndexer(pathForIndex);
        IndexerRunner.of(pathForIndex, supplier, Source.SIMPLE_MODEL).runIndexer();
    }
}
