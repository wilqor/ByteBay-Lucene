package com.wilqor.workshop.bytebay.lucene.wikipediasearch;

import com.wilqor.workshop.bytebay.lucene.config.ConfigLoader;
import com.wilqor.workshop.bytebay.lucene.config.IndexType;
import com.wilqor.workshop.bytebay.lucene.source.Source;
import com.wilqor.workshop.bytebay.lucene.source.model.WikipediaPage;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
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

    private static Logger LOG = LoggerFactory.getLogger(LuceneWikipediaSearcher.class);

    @Override
    public List<SearchResultEntry> search(String searchString) {
        try {
            Path pathForIndex = ConfigLoader.LOADER.getPathForIndex(IndexType.WIKIPEDIA_SEARCH);
            FSDirectory directory = FSDirectory.open(pathForIndex);
            DirectoryReader reader = DirectoryReader.open(directory);
            IndexSearcher indexSearcher = new IndexSearcher(reader);

            HashMap<String, Float> boosts = new HashMap<>();
            boosts.put("title", 100.0f);
            boosts.put("text", 1.0f);


            MultiFieldQueryParser queryParser = new MultiFieldQueryParser(new String[] {"title", "text"},
            new StandardAnalyzer(), boosts);
            Query query = queryParser.parse(searchString);
            TopDocs search = indexSearcher.search(query, 100);

            return Arrays.stream(search.scoreDocs)
                    .map(doc -> {
                                try {

                                   Document document = reader.document(doc.doc);
                                   return SearchResultEntry.builder()
                                            .link(document.get("link"))
                                            .description(document.get("description"))
                                            .title(document.get("title"))
                                            .build();
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                    )
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void indexWikipedia() {
        try {
            long start = System.currentTimeMillis();
            Path pathForIndex = ConfigLoader.LOADER.getPathForIndex(IndexType.WIKIPEDIA_SEARCH);
            if (!pathForIndex.toFile().exists()) {
                FSDirectory directory = FSDirectory.open(pathForIndex);
                StandardAnalyzer analyzer = new StandardAnalyzer();
                IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer).setOpenMode(IndexWriterConfig.OpenMode.CREATE);
                IndexWriter indexWriter = new IndexWriter(directory, indexWriterConfig);

                Source.WIKIPEDIA_PAGE_MODEL.stream()
                        .map(page -> toDocument(page, analyzer))
                        .forEach(document -> {
                            try {
                                indexWriter.addDocument(document);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        });

                indexWriter.commit();

                LOG.info("Indexing took {} ms, indexed {} documents.", System.currentTimeMillis() - start, indexWriter.numDocs());
            } else {
                LOG.info("Index already exists.");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


    private Document toDocument(WikipediaPage page, Analyzer analyzer) {
        Document document = new Document();
        document.add(new StoredField("text", page.getText()));
        document.add(new StoredField("description", page.getDescription()));
        document.add(new StoredField("url", page.getUrl()));
        document.add(new StoredField("title", page.getTitle()));
        document.add(new StoredField("link", page.getUrl()));
        document.add(new TextField("text", analyzer.tokenStream("text", page.getText())));
        document.add(new TextField("title", analyzer.tokenStream("title", page.getText())));

        return document;
    }


    public static void main(String[] args) {
        LuceneWikipediaSearcher searcher = new LuceneWikipediaSearcher();
        searcher.indexWikipedia();

    }
}
