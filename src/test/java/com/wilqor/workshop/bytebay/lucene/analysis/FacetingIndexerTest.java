package com.wilqor.workshop.bytebay.lucene.analysis;

import com.wilqor.workshop.bytebay.lucene.FSDirectoryReadingTest;
import com.wilqor.workshop.bytebay.lucene.config.ConfigLoader;
import com.wilqor.workshop.bytebay.lucene.config.IndexType;
import com.wilqor.workshop.bytebay.lucene.source.model.Thumb;
import org.apache.lucene.facet.FacetResult;
import org.apache.lucene.facet.Facets;
import org.apache.lucene.facet.FacetsCollector;
import org.apache.lucene.facet.LabelAndValue;
import org.apache.lucene.facet.range.LongRange;
import org.apache.lucene.facet.range.LongRangeFacetCounts;

import org.apache.lucene.facet.sortedset.DefaultSortedSetDocValuesReaderState;
import org.apache.lucene.facet.sortedset.SortedSetDocValuesFacetCounts;
import org.apache.lucene.facet.sortedset.SortedSetDocValuesReaderState;
import org.apache.lucene.index.Term;
import org.apache.lucene.queries.function.ValueSource;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.grouping.GroupingSearch;
import org.apache.lucene.search.grouping.TopGroups;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.arrayWithSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class FacetingIndexerTest extends FSDirectoryReadingTest {
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
        FacetResult topArticlesResult = getFacetResult(new MatchAllDocsQuery(), FacetingIndexer.ARTICLE_FIELD);

        LabelAndValue[] labelValues = topArticlesResult.labelValues;
        LabelAndValue topArticle = labelValues[0];
        assertThat(topArticle.label, is("Lucene 101"));
        assertThat(topArticle.value, is(2));
    }

    @Test
    public void shouldFindMostActiveUser() throws Exception {
        FacetResult topUsersResult = getFacetResult(new MatchAllDocsQuery(), FacetingIndexer.USER_NAME_FIELD);

        LabelAndValue[] labelValues = topUsersResult.labelValues;
        LabelAndValue topUser = labelValues[0];
        assertThat(topUser.label, is("zbyszkop"));
        assertThat(topUser.value, is(3));
    }

    @Test
    public void shouldGroupByTimestamp() throws Exception {

        long lowest = 1510422743;
        long highest = 1520422743;
        int noOfGroups = 10000;

        LongRange[] ranges = getRanges(lowest, highest, noOfGroups);

        FacetsCollector fc = new FacetsCollector();
        FacetsCollector.search(searcher, new MatchAllDocsQuery(), noOfGroups, fc);
        FacetResult result = getFacetResult(ranges, fc);
        FacetResult resultLowerRes = getFacetResult(getRanges(lowest, highest, noOfGroups / 10), fc);
        for (int i = 0; i < result.childCount; i++) {
            LabelAndValue lv = result.labelValues[i];

            System.out.println(String.format("%s (%s)", lv.label, lv.value));
        }
        System.out.println(result.childCount);
    }

    private FacetResult getFacetResult(LongRange[] ranges, FacetsCollector fc) throws IOException {
        LongRangeFacetCounts facets = new LongRangeFacetCounts(FacetingIndexer.TIMESTAMP_FIELD, fc, ranges);
        return facets.getTopChildren(0, FacetingIndexer.TIMESTAMP_FIELD);
    }

    private LongRange[] getRanges(long lowest, long highest, int noOfGroups) {
        long rangeSize = (highest - lowest) / noOfGroups;

        return IntStream.range(0, noOfGroups)
                .mapToObj(rangeId -> {
                    long rangeStart = lowest + rangeId * rangeSize;
                    long rangeEnd = rangeStart + rangeSize;
                    return new LongRange(rangeStart + "-" + rangeEnd, rangeStart, true, rangeEnd, false);
                }).toArray(LongRange[]::new);
    }

    @Test
    public void shouldCountThumbsForArticle() throws Exception {
        TermQuery articleFilterQuery = new TermQuery(new Term(FacetingIndexer.ARTICLE_FIELD, "Lucene best practices"));
        FacetResult topThumbsResult = getFacetResult(articleFilterQuery, FacetingIndexer.THUMB_FIELD);

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

//    private FacetResult getFacetLongRangeResult(Query query, String facetFieldName) throws IOException {
//        LongRange longRange = new LongRange(facetFieldName + " range", 0, false, System.currentTimeMillis(), true);
//
//
//    }
}