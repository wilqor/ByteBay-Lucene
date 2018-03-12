package com.wilqor.workshop.bytebay.lucene.analysis;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;

import com.wilqor.workshop.bytebay.lucene.indexing.Indexer;
import com.wilqor.workshop.bytebay.lucene.indexing.SimpleReviewIndexer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.KeywordTokenizer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.IOUtils;

import com.wilqor.workshop.bytebay.lucene.config.ConfigLoader;
import com.wilqor.workshop.bytebay.lucene.config.IndexType;
import com.wilqor.workshop.bytebay.lucene.source.Source;
import com.wilqor.workshop.bytebay.lucene.source.model.SimpleReview;

public class KeywordTokenizerExample {

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
