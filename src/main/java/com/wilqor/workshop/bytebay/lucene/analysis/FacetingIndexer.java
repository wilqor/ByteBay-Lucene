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
import org.apache.lucene.facet.FacetField;
import org.apache.lucene.facet.FacetsConfig;
import org.apache.lucene.facet.taxonomy.TaxonomyWriter;
import org.apache.lucene.facet.taxonomy.directory.DirectoryTaxonomyWriter;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.IOUtils;

import java.io.IOException;
import java.nio.file.Path;

public class FacetingIndexer implements Indexer<SimpleReview> {
    static final FacetsConfig FACETS_CONFIG = new FacetsConfig();
    static final String INDEX_SUBDIRECTORY_NAME = "index";
    static final String TAXONOMY_SUBDIRECTORY_NAME = "taxonomy";

    static final String USER_NAME_FACET_FIELD = "user_name";
    static final String THUMB_FACET_FIELD = "thumb";
    static final String ARTICLE_FACET_FIELD = "article_name";

    private static final Logger LOGGER = LogManager.getLogger(FacetingIndexer.class);

    private final FSDirectory directory;
    private final FSDirectory taxonomyDirectory;
    private final IndexWriter indexWriter;
    private final TaxonomyWriter taxonomyWriter;
    private final Analyzer analyzer;

    FacetingIndexer(Path targetDirectory) throws IOException {
        directory = FSDirectory.open(targetDirectory.resolve(INDEX_SUBDIRECTORY_NAME));
        LOGGER.info("Opened index in directory: {}", directory.getDirectory());
        taxonomyDirectory = FSDirectory.open(targetDirectory.resolve(TAXONOMY_SUBDIRECTORY_NAME));
        taxonomyWriter = new DirectoryTaxonomyWriter(taxonomyDirectory);
        LOGGER.info("Opened taxonomy in directory: {}", taxonomyDirectory.getDirectory());
        analyzer = new KeywordAnalyzer();
        indexWriter = new IndexWriter(directory, new IndexWriterConfig(analyzer));
    }

    @Override
    public void index(Source<SimpleReview> source) {
        source.stream().forEach(simpleReview -> {
            Document reviewDocument = mapToDocument(simpleReview);
            try {
                Document facetedDocument = FACETS_CONFIG.build(taxonomyWriter, reviewDocument);
                indexWriter.addDocument(facetedDocument);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private Document mapToDocument(SimpleReview simpleReview) {
        Document reviewDocument = new Document();
        reviewDocument.add(new FacetField(USER_NAME_FACET_FIELD, simpleReview.getUserName()));
        reviewDocument.add(new FacetField(THUMB_FACET_FIELD, simpleReview.getThumb().name()));
        reviewDocument.add(new StringField(ARTICLE_FACET_FIELD, simpleReview.getArticleName(), Field.Store.YES));
        reviewDocument.add(new FacetField(ARTICLE_FACET_FIELD, simpleReview.getArticleName()));
        return reviewDocument;
    }

    @Override
    public void close() throws Exception {
        Path directoryPath = directory.getDirectory();
        Path taxonomyPath = taxonomyDirectory.getDirectory();
        IOUtils.close(indexWriter, taxonomyWriter, directory, taxonomyDirectory, analyzer);
        LOGGER.info("Closed index in directory: {}", directoryPath);
        LOGGER.info("Closed taxonomy in directory: {}", taxonomyPath);
    }

    public static void main(String[] args) throws Exception {
        try (Indexer<SimpleReview> indexer = new FacetingIndexer(ConfigLoader.LOADER.getPathForIndex(IndexType.FACETING_EXAMPLE))) {
            indexer.index(Source.SIMPLE_MODEL);
        }
    }
}
