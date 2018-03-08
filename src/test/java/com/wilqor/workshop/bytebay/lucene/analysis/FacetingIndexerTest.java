package com.wilqor.workshop.bytebay.lucene.analysis;

import static org.hamcrest.Matchers.arrayWithSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.nio.file.Path;

import org.apache.lucene.facet.FacetResult;
import org.apache.lucene.facet.Facets;
import org.apache.lucene.facet.FacetsCollector;
import org.apache.lucene.facet.LabelAndValue;
import org.apache.lucene.facet.sortedset.DefaultSortedSetDocValuesReaderState;
import org.apache.lucene.facet.sortedset.SortedSetDocValuesFacetCounts;
import org.apache.lucene.facet.sortedset.SortedSetDocValuesReaderState;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.junit.Test;

import com.wilqor.workshop.bytebay.lucene.BaseReadingTest;
import com.wilqor.workshop.bytebay.lucene.config.ConfigLoader;
import com.wilqor.workshop.bytebay.lucene.config.IndexType;
import com.wilqor.workshop.bytebay.lucene.source.model.SimpleReview;
import com.wilqor.workshop.bytebay.lucene.source.model.Thumb;

public class FacetingIndexerTest extends BaseReadingTest {
    private static final int TOP_N_LIMIT = 3;

    private SortedSetDocValuesReaderState state;

    @Override
    protected Path provideDirectoryPath() {
        return ConfigLoader.LOADER.getPathForIndex(IndexType.FACETING_EXAMPLE);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        state = new DefaultSortedSetDocValuesReaderState(searcher.getIndexReader());
    }

    @Test
    public void shouldFindMostCommentedArticle() throws Exception {
        FacetResult topArticlesResult = getFacetResult(new MatchAllDocsQuery(), SimpleReview.ARTICLE_NAME_FIELD);

        LabelAndValue[] labelValues = topArticlesResult.labelValues;
        LabelAndValue topArticle = labelValues[0];
        assertThat(topArticle.label, is("Lucene 101"));
        assertThat(topArticle.value, is(2));
    }

    @Test
    public void shouldFindMostActiveUser() throws Exception {
        FacetResult topUsersResult = getFacetResult(new MatchAllDocsQuery(), SimpleReview.USER_NAME_FIELD);

        LabelAndValue[] labelValues = topUsersResult.labelValues;
        LabelAndValue topUser = labelValues[0];
        assertThat(topUser.label, is("zbyszkop"));
        assertThat(topUser.value, is(3));
    }

    @Test
    public void shouldCountThumbsForArticle() throws Exception {
        TermQuery articleFilterQuery = new TermQuery(new Term(SimpleReview.ARTICLE_NAME_FIELD, "Lucene best practices"));
        FacetResult topThumbsResult = getFacetResult(articleFilterQuery, SimpleReview.THUMB_FIELD);

        LabelAndValue[] labelValues = topThumbsResult.labelValues;
        assertThat(labelValues, is(arrayWithSize(1)));

        LabelAndValue topThumbs = labelValues[0];
        assertThat(topThumbs.label, is(Thumb.UP.name()));
        assertThat(topThumbs.value, is(2));
    }

    private FacetResult getFacetResult(Query query, String facetFieldName) throws IOException {
        FacetsCollector facetsCollector = new FacetsCollector();
        FacetsCollector.search(searcher, query, 0, facetsCollector);
        Facets facets = new SortedSetDocValuesFacetCounts(state, facetsCollector);
        return facets.getTopChildren(TOP_N_LIMIT, facetFieldName);
    }
}