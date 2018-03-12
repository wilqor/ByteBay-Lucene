package com.wilqor.workshop.bytebay.lucene.analysis;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.KeywordTokenizer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.IOUtils;

import com.wilqor.workshop.bytebay.lucene.config.ConfigLoader;
import com.wilqor.workshop.bytebay.lucene.config.IndexType;
import com.wilqor.workshop.bytebay.lucene.source.Source;
import com.wilqor.workshop.bytebay.lucene.source.model.SimpleReview;

public class KeywordTokenizerExample {
	public static class SimpleReviewIndexer implements Indexer<SimpleReview> {

		private static final Logger LOGGER = LogManager.getLogger(SimpleReviewIndexer.class);

		private final FSDirectory directory;
		private final Analyzer analyzer;
		private final IndexWriter indexWriter;

		SimpleReviewIndexer(Path targetDirectory, Analyzer analyzer) throws IOException {
			directory = FSDirectory.open(targetDirectory);
			LOGGER.info("Opened index in directory: {}", directory.getDirectory());
			this.analyzer = analyzer;
			IndexWriterConfig indexWriterConfig = new IndexWriterConfig(this.analyzer)
					.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
			indexWriter = new IndexWriter(directory, indexWriterConfig);
		}

		@Override
		public void index(Source<SimpleReview> source) {
			source.stream().forEach(sourceItem -> {
				Document sourceDocument = mapToDocument(sourceItem);
				try {
					indexWriter.addDocument(sourceDocument);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			});
		}

		Document mapToDocument(SimpleReview sourceItem) {
			Document reviewDocument = new Document();
			reviewDocument.add(new StringField(SimpleReview.USER_NAME_FIELD, sourceItem.getUserName(), Field.Store.YES));
			reviewDocument.add(new StringField(SimpleReview.THUMB_FIELD, sourceItem.getThumb().name(), Field.Store.YES));
			reviewDocument.add(new TextField(SimpleReview.ARTICLE_NAME_FIELD, sourceItem.getArticleName(), Field.Store.YES));
			return reviewDocument;
		}

		@Override
		public void close() throws Exception {
			Path directoryPath = directory.getDirectory();
			IOUtils.close(indexWriter, directory, analyzer);
			LOGGER.info("Closed index in directory: {}", directoryPath);
		}
	}

	private static class KeywordTokenizingAnalyzer extends Analyzer {
		@Override
		protected TokenStreamComponents createComponents(String fieldName) {
			Tokenizer tokenizer = new KeywordTokenizer();
			return new TokenStreamComponents(tokenizer);
		}
	}

	private static class SimpleReviewReader implements AutoCloseable {
		private static final Logger LOGGER = LogManager.getLogger(SimpleReviewReader.class);
		private static final int QUERY_MATCHES_LIMIT = 10;

		private Directory directory;
		private IndexReader reader;
		private IndexSearcher searcher;

		SimpleReviewReader(Path directoryPath) throws IOException {
			directory = FSDirectory.open(directoryPath);
			reader = DirectoryReader.open(directory);
			searcher = new IndexSearcher(reader);
		}

		void read() throws IOException {
			TopDocs topDocs = searcher.search(new MatchAllDocsQuery(), QUERY_MATCHES_LIMIT);
			Arrays.stream(topDocs.scoreDocs).forEach(scoreDoc -> {
				try {
					Document review = searcher.doc(scoreDoc.doc);
					LOGGER.info("Found review - user name: {}, thumb: {}, article name: {}",
							review.get(SimpleReview.USER_NAME_FIELD),
							review.get(SimpleReview.THUMB_FIELD),
							review.get(SimpleReview.ARTICLE_NAME_FIELD)
					);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			});
		}

		@Override
		public void close() throws Exception {
			IOUtils.close(reader, directory);
		}
	}

	public static void main(String[] args) throws Exception {
		Path pathForIndex = ConfigLoader.LOADER.getPathForIndex(IndexType.KEYWORD_TOKENIZER_EXAMPLE);
		try (Indexer<SimpleReview> indexer = new SimpleReviewIndexer(pathForIndex, new KeywordTokenizingAnalyzer())) {
			indexer.index(Source.SIMPLE_MODEL);
		}
		try (SimpleReviewReader reader = new SimpleReviewReader(pathForIndex)) {
			reader.read();
		}
	}
}
