package com.wilqor.workshop.bytebay.lucene.analysis;

import java.io.IOException;
import java.nio.file.Path;

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

import com.wilqor.workshop.bytebay.lucene.config.ConfigLoader;
import com.wilqor.workshop.bytebay.lucene.config.IndexType;
import com.wilqor.workshop.bytebay.lucene.source.Source;
import com.wilqor.workshop.bytebay.lucene.source.model.SimpleReview;

public class FacetingExample {
	public static class FacetingIndexer implements Indexer<SimpleReview> {
		private static final Logger LOGGER = LogManager.getLogger(FacetingExample.class);

		private final FSDirectory directory;
		private final IndexWriter indexWriter;
		private final Analyzer analyzer;

		FacetingIndexer(Path targetDirectory) throws IOException {
			directory = FSDirectory.open(targetDirectory);
			LOGGER.info("Opened index in directory: {}", directory.getDirectory());
			analyzer = new KeywordAnalyzer();
			IndexWriterConfig indexWriterConfig = new IndexWriterConfig(this.analyzer)
					.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
			indexWriter = new IndexWriter(directory, indexWriterConfig);
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
			reviewDocument.add(new SortedSetDocValuesFacetField(SimpleReview.USER_NAME_FIELD, simpleReview.getUserName()));
			reviewDocument.add(new SortedSetDocValuesFacetField(SimpleReview.THUMB_FIELD, simpleReview.getThumb().name()));
			reviewDocument.add(new StringField(SimpleReview.ARTICLE_NAME_FIELD, simpleReview.getArticleName(), Field.Store.YES));
			reviewDocument.add(new SortedSetDocValuesFacetField(SimpleReview.ARTICLE_NAME_FIELD, simpleReview.getArticleName()));
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
		try (Indexer<SimpleReview> indexer = new FacetingIndexer(pathForIndex)) {
			indexer.index(Source.SIMPLE_MODEL);
		}
	}
}
