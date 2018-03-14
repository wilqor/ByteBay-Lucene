package com.wilqor.workshop.bytebay.lucene.wikipediasearch;

import com.wilqor.workshop.bytebay.lucene.config.ConfigLoader;
import com.wilqor.workshop.bytebay.lucene.config.IndexType;
import com.wilqor.workshop.bytebay.lucene.source.Source;
import com.wilqor.workshop.bytebay.lucene.source.model.WikipediaPage;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class LuceneWikipediaSearcher implements WikipediaSearcher {
    private static final Logger LOGGER = LoggerFactory.getLogger(LuceneWikipediaSearcher.class);
    public static final int TOP_RESULTS_LIMIT = 100;

    private final Path directoryPath;

    public LuceneWikipediaSearcher() {
        directoryPath = ConfigLoader.LOADER.getPathForIndex(IndexType.WIKIPEDIA_SEARCH);
    }

    @Override
    public void indexWikipedia() {
        // TODO use doIndex method to perform actual indexing
        doIndex();
    }

    private void doIndex() {
        if (!directoryPath.toFile().exists()) {
            try (Directory directory = FSDirectory.open(directoryPath);
                 Analyzer analyzer = buildAnalyzer();
                 IndexWriter writer = new IndexWriter(directory, new IndexWriterConfig(analyzer).setOpenMode(IndexWriterConfig.OpenMode.CREATE))) {
                long start = System.currentTimeMillis();
                Source.WIKIPEDIA_PAGE_MODEL.stream().forEach(wikipediaPage -> {
                    try {
                        writer.addDocument(mapToDocument(wikipediaPage));
                    } catch (IOException e) {
                        LOGGER.warn("Writing document to index failed", e);
                        throw new RuntimeException(e);
                    }
                });
                writer.commit();
                LOGGER.info("Indexing took {} ms, indexed {} documents.", System.currentTimeMillis() - start, writer.numDocs());
            } catch (Exception e) {
                LOGGER.warn("Indexing failed", e);
                throw new RuntimeException(e);
            }
        } else {
            LOGGER.info("Index already exists");
        }
    }

    // TODO build analyzer chain
    // - what kind of tokenization should be performed?
    // - what kind of token filtering should be performed?
    private Analyzer buildAnalyzer() {
        return new StandardAnalyzer();
    }

    // TODO map page to document
    // - which kind of fields to use?
    // - which object fields should be stored to allow retrieval?
    private Document mapToDocument(WikipediaPage wikipediaPage) {
        Document document = new Document();
        document.add(new StoredField("description", wikipediaPage.getDescription()));
        document.add(new StoredField("url", wikipediaPage.getUrl()));
        document.add(new StoredField("link", wikipediaPage.getUrl()));
        document.add(new TextField("text", wikipediaPage.getText(), Field.Store.NO));
        document.add(new TextField("title", wikipediaPage.getTitle(), Field.Store.YES));
        return document;
    }

    @Override
    public List<SearchResultEntry> search(String searchString) {
        // TODO use doSearch method to perform actual search
        return doSearch(searchString);
    }

    public List<SearchResultEntry> doSearch(String searchString) {
        try (Directory directory = FSDirectory.open(directoryPath);
             IndexReader reader = DirectoryReader.open(directory)) {
            IndexSearcher searcher = new IndexSearcher(reader);
            TopDocs topDocs = searcher.search(buildQuery(searchString), TOP_RESULTS_LIMIT);
            return Arrays.stream(topDocs.scoreDocs)
                    .map(topDoc -> {
                        try {
                            Document document = searcher.doc(topDoc.doc);
                            return mapToSearchResultEntry(document);
                        } catch (IOException e) {
                            LOGGER.warn("Retrieving document from index failed", e);
                            throw new RuntimeException(e);
                        }
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            LOGGER.warn("Searching entries in index failed", e);
            throw new RuntimeException(e);
        }
    }

    // TODO build Query from string
    // - which parser to use?
    // - how to parse search string?
    // - which fields should be used for search?
    private Query buildQuery(String searchString) {
        HashMap<String, Float> boosts = new HashMap<>();
        boosts.put("title", 100.0f);
        boosts.put("text", 1.0f);
        MultiFieldQueryParser queryParser = new MultiFieldQueryParser(new String[]{"title", "text"},
                new StandardAnalyzer(), boosts);
        try {
            return queryParser.parse(searchString);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    // TODO map from Document to a domain object to present to the user
    // - which fields in the index should be used?
    private SearchResultEntry mapToSearchResultEntry(Document document) {
        return SearchResultEntry.builder()
                .link(document.get("link"))
                .description(document.get("description"))
                .title(document.get("title"))
                .build();
    }
}
