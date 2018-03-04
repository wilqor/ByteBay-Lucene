package com.wilqor.workshop.bytebay.lucene.analysis;

import com.wilqor.workshop.bytebay.lucene.source.Source;
import com.wilqor.workshop.bytebay.lucene.utils.ThrowingSupplier;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.IOUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class IndexerRunnerTest {
    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    private IndexerRunner<DummyModel> indexerRunner;
    private Path indexRootDirectory;

    @Before
    public void setUp() throws Exception {
        indexRootDirectory = temporaryFolder.newFolder().toPath();
        ThrowingSupplier<Indexer<DummyModel>> supplier = () -> new DummyIndexer(indexRootDirectory);
        indexerRunner = IndexerRunner.of(
                indexRootDirectory,
                supplier,
                () -> Stream.of(
                        new DummyModel("1"),
                        new DummyModel("2"),
                        new DummyModel("3")
                )
        );
    }

    @Test
    public void shouldKeepResultOfOneIndexingAfterMultipleRuns() throws Exception {
        indexerRunner.runIndexer();
        indexerRunner.runIndexer();
        TopDocs topDocs = searchForAllDocumentsInIndex();
        assertThat(topDocs.totalHits, is(3L));

    }

    private TopDocs searchForAllDocumentsInIndex() throws Exception {
        Directory directory = FSDirectory.open(indexRootDirectory);
        IndexReader reader = DirectoryReader.open(directory);
        IndexSearcher searcher = new IndexSearcher(reader);
        TopDocs topDocs = searcher.search(new MatchAllDocsQuery(), 10);
        IOUtils.close(reader, directory);
        return topDocs;
    }

    private static final class DummyModel {
        private final String id;

        private DummyModel(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }
    }

    private static class DummyIndexer implements Indexer<DummyModel> {
        private final Directory directory;

        private DummyIndexer(Path indexRootDirectory) throws IOException {
            directory = FSDirectory.open(indexRootDirectory);
        }

        @Override
        public void index(Source<DummyModel> source) {
            Analyzer analyzer = new KeywordAnalyzer();
            IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
            try (IndexWriter writer = new IndexWriter(directory, indexWriterConfig)) {
                source.stream().forEach(
                        dummyModel -> {
                            Document document = new Document();
                            document.add(new StringField("id", dummyModel.getId(), Field.Store.YES));
                            try {
                                writer.addDocument(document);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                );
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void close() throws Exception {
            directory.close();
        }
    }
}