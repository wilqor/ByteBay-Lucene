package com.wilqor.workshop.bytebay.lucene.analysis;

import java.io.Reader;
import java.nio.file.Path;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.charfilter.HTMLStripCharFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;

import com.wilqor.workshop.bytebay.lucene.config.ConfigLoader;
import com.wilqor.workshop.bytebay.lucene.config.IndexType;
import com.wilqor.workshop.bytebay.lucene.source.Source;
import com.wilqor.workshop.bytebay.lucene.source.model.CommentedReview;

public class HTMLStripCharFilterExample {
	private static class HTMLStrippingAnalyzer extends Analyzer {
		@Override
		protected Reader initReader(String fieldName, Reader reader) {
			return new HTMLStripCharFilter(reader);
		}

		@Override
		protected TokenStreamComponents createComponents(String fieldName) {
			Tokenizer tokenizer = new StandardTokenizer();
			return new TokenStreamComponents(tokenizer);
		}
	}

	public static void main(String[] args) throws Exception {
		Path pathForIndex = ConfigLoader.LOADER.getPathForIndex(IndexType.HTML_CHAR_STRIP_FILTER_EXAMPLE);
		try (Indexer<CommentedReview> indexer = new WhitespaceTokenizerExample.CommentedReviewIndexer(pathForIndex,
				new HTMLStrippingAnalyzer())) {
			indexer.index(Source.COMMENTED_HTML_MODEL);
		}
	}
}
