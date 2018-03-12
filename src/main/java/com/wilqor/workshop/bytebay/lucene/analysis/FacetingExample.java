package com.wilqor.workshop.bytebay.lucene.analysis;

import com.wilqor.workshop.bytebay.lucene.config.ConfigLoader;
import com.wilqor.workshop.bytebay.lucene.config.IndexType;
import com.wilqor.workshop.bytebay.lucene.indexing.Indexer;
import com.wilqor.workshop.bytebay.lucene.source.Source;
import com.wilqor.workshop.bytebay.lucene.source.model.CommentedReviewWithTimestamp;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.NumericDocValuesField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.facet.FacetsConfig;
import org.apache.lucene.facet.sortedset.SortedSetDocValuesFacetField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.IOUtils;

import java.io.IOException;
import java.nio.file.Path;

public class FacetingExample {
	public static class FacetingIndexer implements Indexer<CommentedReviewWithTimestamp> {
		private static final Logger LOGGER = LogManager.getLogger(FacetingExample.class);

		private final FSDirectory directory;
		private final IndexWriter indexWriter;
		private final Analyzer analyzer;
		private FacetsConfig facetsConfig;

		FacetingIndexer(Path targetDirectory) throws IOException {
			directory = FSDirectory.open(targetDirectory);
			LOGGER.info("Opened index in directory: {}", directory.getDirectory());
			analyzer = new KeywordAnalyzer();
			IndexWriterConfig indexWriterConfig = new IndexWriterConfig(this.analyzer)
					.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
			indexWriter = new IndexWriter(directory, indexWriterConfig);
			facetsConfig = new FacetsConfig();
		}

		@Override
		public void index(Source<CommentedReviewWithTimestamp> source) {
			source.stream().forEach(simpleReview -> {
				Document reviewDocument = mapToDocument(simpleReview);
				try {
					Document facetedDocument = facetsConfig.build(reviewDocument);
					indexWriter.addDocument(facetedDocument);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			});
		}

		private Document mapToDocument(CommentedReviewWithTimestamp simpleReview) {
			Document reviewDocument = new Document();
			reviewDocument.add(new SortedSetDocValuesFacetField(CommentedReviewWithTimestamp.USER_NAME_FIELD, simpleReview.getUserName()));
			reviewDocument.add(new SortedSetDocValuesFacetField(CommentedReviewWithTimestamp.THUMB_FIELD, simpleReview.getThumb().name()));
			reviewDocument.add(new StringField(CommentedReviewWithTimestamp.ARTICLE_NAME_FIELD, simpleReview.getArticleName(), Field.Store.YES));
			reviewDocument.add(new SortedSetDocValuesFacetField(CommentedReviewWithTimestamp.ARTICLE_NAME_FIELD, simpleReview.getArticleName()));
			reviewDocument.add(new NumericDocValuesField(CommentedReviewWithTimestamp.TIMESTAMP_FIELD, simpleReview.getTimestamp()));
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
		Path pathForIndex = ConfigLoader.LOADER.getPathForIndex(IndexType.FACETING_EXAMPLE);
		try (Indexer<CommentedReviewWithTimestamp> indexer = new FacetingIndexer(pathForIndex)) {
			indexer.index(Source.COMMENTED_WITH_TIMESTAMP_MODEL);
		}
	}
}
