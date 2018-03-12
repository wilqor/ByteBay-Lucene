package com.wilqor.workshop.bytebay.lucene.analysis;

import com.wilqor.workshop.bytebay.lucene.BaseReadingTest;
import com.wilqor.workshop.bytebay.lucene.config.ConfigLoader;
import com.wilqor.workshop.bytebay.lucene.config.IndexType;
import com.wilqor.workshop.bytebay.lucene.indexing.Indexer;
import com.wilqor.workshop.bytebay.lucene.source.Source;
import com.wilqor.workshop.bytebay.lucene.source.model.CommentedReview;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.junit.Test;

import java.nio.file.Path;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class HTMLStripCharFilterExampleTest extends BaseReadingTest {
	private static final int QUERY_MATCHES_LIMIT = 3;

	@Override
	public void setUp() throws Exception {
		try (Indexer<CommentedReview> indexer = HTMLStripCharFilterExample.getIndexerForPath(provideDirectoryPath())) {
			indexer.index(Source.COMMENTED_HTML_MODEL);
		}
		super.setUp();
	}

	@Override
	protected Path provideDirectoryPath() {
		return ConfigLoader.LOADER.getPathForIndex(IndexType.HTML_CHAR_STRIP_FILTER_EXAMPLE);
	}

	@Test
	public void shouldRetrieveCommentsForTermInsideHTMLTag() throws Exception {
		Query query = new TermQuery(new Term(CommentedReview.COMMENT_FIELD, "polecam"));
		TopDocs topDocs = searcher.search(query, QUERY_MATCHES_LIMIT);

		assertThat(topDocs.totalHits, is(2L));
	}

	@Test
	public void shouldRetrieveCommentsForTermInsideNestedHTMLTagWithAttribute() throws Exception {
		Query query = new TermQuery(new Term(CommentedReview.COMMENT_FIELD, "Serdecznie"));
		TopDocs topDocs = searcher.search(query, QUERY_MATCHES_LIMIT);

		assertThat(topDocs.totalHits, is(1L));
	}

	@Test
	public void shouldRetrieveZeroCommentsForHTMLAttribute() throws Exception {
		Query query = new TermQuery(new Term(CommentedReview.COMMENT_FIELD, "highlighted"));
		TopDocs topDocs = searcher.search(query, QUERY_MATCHES_LIMIT);

		assertThat(topDocs.totalHits, is(0L));
	}

	@Test
	public void shouldRetrieveZeroCommentsForHTMLTagName() throws Exception {
		Query query = new TermQuery(new Term(CommentedReview.COMMENT_FIELD, "strong"));
		TopDocs topDocs = searcher.search(query, QUERY_MATCHES_LIMIT);

		assertThat(topDocs.totalHits, is(0L));
	}

	@Test
	public void shouldRetrieveZeroCommentsForHTMLOpeningTag() throws Exception {
		Query query = new TermQuery(new Term(CommentedReview.COMMENT_FIELD, "<p>"));
		TopDocs topDocs = searcher.search(query, QUERY_MATCHES_LIMIT);

		assertThat(topDocs.totalHits, is(0L));
	}

	@Test
	public void shouldRetrieveZeroCommentsForHTMLClosingTag() throws Exception {
		Query query = new TermQuery(new Term(CommentedReview.COMMENT_FIELD, "</p>"));
		TopDocs topDocs = searcher.search(query, QUERY_MATCHES_LIMIT);

		assertThat(topDocs.totalHits, is(0L));
	}

	@Test
	public void shouldRetrieveZeroCommentsForHTMLTagWithContent() throws Exception {
		Query query = new TermQuery(
				new Term(CommentedReview.COMMENT_FIELD, "<strong>polecam</strong>"));
		TopDocs topDocs = searcher.search(query, QUERY_MATCHES_LIMIT);

		assertThat(topDocs.totalHits, is(0L));
	}
}