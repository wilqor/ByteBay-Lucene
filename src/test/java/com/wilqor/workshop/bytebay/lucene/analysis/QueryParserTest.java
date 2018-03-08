package com.wilqor.workshop.bytebay.lucene.analysis;

import com.wilqor.workshop.bytebay.lucene.BaseReadingTest;
import com.wilqor.workshop.bytebay.lucene.config.ConfigLoader;
import com.wilqor.workshop.bytebay.lucene.config.IndexType;
import com.wilqor.workshop.bytebay.lucene.source.model.CommentedReview;
import com.wilqor.workshop.bytebay.lucene.source.model.SimpleReview;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class QueryParserTest extends BaseReadingTest {
    private static final int TOP_HITS_LIMIT = 3;
    private static final String DEFAULT_SEARCH_FIELD = CommentedReview.COMMENT_FIELD;

    private Analyzer queryAnalyzer;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        queryAnalyzer = new WhitespaceAnalyzer();
    }

    @Override
    protected Path provideDirectoryPath() {
        return ConfigLoader.LOADER.getPathForIndex(IndexType.WHITESPACE_TOKENIZER_EXAMPLE);
    }

    @Test
    public void shouldRetrieveReviewsByTermInDefaultSearchField() throws Exception {
        QueryParser queryParser = new QueryParser(DEFAULT_SEARCH_FIELD, queryAnalyzer);
        Query query = queryParser.parse("polecam");
        TopDocs topDocs = searcher.search(query, TOP_HITS_LIMIT);

        assertThat(topDocs.totalHits, is(2L));
    }

    @Test
    public void shouldRetrieveReviewsByPhraseInExplicitCommentField() throws Exception {
        QueryParser queryParser = new QueryParser(DEFAULT_SEARCH_FIELD, new WhitespaceAnalyzer());
        Query query = queryParser.parse(DEFAULT_SEARCH_FIELD + ":" + "\"Serdecznie polecam\"");

        TopDocs topDocs = searcher.search(query, TOP_HITS_LIMIT);

        assertThat(topDocs.totalHits, is(1L));

    }

    @Test
    public void shouldRetrieveReviewsByMultiFieldSearch() throws Exception {
        MultiFieldQueryParser queryParser = new MultiFieldQueryParser(
                new String[]{DEFAULT_SEARCH_FIELD, SimpleReview.ARTICLE_NAME_FIELD},
                queryAnalyzer);
        Query query = queryParser.parse("polecam best");
        TopDocs topDocs = searcher.search(query, TOP_HITS_LIMIT);

        assertThat(topDocs.totalHits, is(4L));
    }

    @Test
    public void shouldRetrieveAllReviewsByTermWithBothKindsOfWildcardInArticleNameField() throws Exception {
        QueryParser queryParser = new QueryParser(DEFAULT_SEARCH_FIELD, queryAnalyzer);
        // TODO define query as specified in test method name
        Query query = queryParser.parse("put your query here");
        TopDocs topDocs = searcher.search(query, TOP_HITS_LIMIT);

        assertThat(topDocs.totalHits, is(5L));
    }

    @Test
    public void shouldRetrieveFirstAndThirdReviewByBooleanQuery() throws Exception {
        QueryParser queryParser = new QueryParser(DEFAULT_SEARCH_FIELD, queryAnalyzer);
        // TODO define query as specified in test method name
        Query query = queryParser.parse("put your query here");
        TopDocs topDocs = searcher.search(query, TOP_HITS_LIMIT);

        assertThat(topDocs.totalHits, is(2L));
        List<Integer> matchedDocIds = Arrays.stream(topDocs.scoreDocs)
                .map(scoreDoc -> scoreDoc.doc)
                .collect(Collectors.toList());
        assertThat(matchedDocIds, containsInAnyOrder(0, 2));
    }
}
