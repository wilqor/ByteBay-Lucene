package com.wilqor.workshop.bytebay.lucene.analysis;

import com.wilqor.workshop.bytebay.lucene.FSDirectoryReadingTest;
import com.wilqor.workshop.bytebay.lucene.config.ConfigLoader;
import com.wilqor.workshop.bytebay.lucene.config.IndexType;
import com.wilqor.workshop.bytebay.lucene.source.model.Thumb;
import org.apache.lucene.facet.FacetResult;
import org.apache.lucene.facet.Facets;
import org.apache.lucene.facet.FacetsCollector;
import org.apache.lucene.facet.LabelAndValue;
import org.apache.lucene.facet.taxonomy.FastTaxonomyFacetCounts;
import org.apache.lucene.facet.taxonomy.TaxonomyReader;
import org.apache.lucene.facet.taxonomy.directory.DirectoryTaxonomyReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Path;

import static org.hamcrest.Matchers.arrayWithSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class FacetingIndexerTest extends FSDirectoryReadingTest {
    private static final int TOP_N_LIMIT = 3;
    private Path directoryPath;
    private Directory taxonomyDirectory;
    private TaxonomyReader taxonomyReader;

    @Override
    protected Path provideDirectoryPath() {
        return directoryPath.resolve(FacetingIndexer.INDEX_SUBDIRECTORY_NAME);
    }

    @Override
    @Before
    public void setUp() throws Exception {
        directoryPath = ConfigLoader.LOADER.getPathForIndex(IndexType.FACETING_EXAMPLE);
        super.setUp();
        taxonomyDirectory = FSDirectory.open(directoryPath.resolve(FacetingIndexer.TAXONOMY_SUBDIRECTORY_NAME));
        taxonomyReader = new DirectoryTaxonomyReader(taxonomyDirectory);
    }

    @Override
    @After
    public void tearDown() throws Exception {
        super.tearDown();
        IOUtils.close(taxonomyDirectory, taxonomyReader);
    }

    @Test
    public void shouldFindMostCommentedArticle() throws Exception {
        FacetResult topArticlesResult = getFacetResult(new MatchAllDocsQuery(), FacetingIndexer.ARTICLE_FACET_FIELD);

        LabelAndValue[] labelValues = topArticlesResult.labelValues;
        LabelAndValue topArticle = labelValues[0];
        assertThat(topArticle.label, is("Lucene best practices"));
        assertThat(topArticle.value, is(2));
    }

    @Test
    public void shouldFindMostActiveUser() throws Exception {
        FacetResult topUsersResult = getFacetResult(new MatchAllDocsQuery(), FacetingIndexer.USER_NAME_FACET_FIELD);

        LabelAndValue[] labelValues = topUsersResult.labelValues;
        LabelAndValue topUser = labelValues[0];
        assertThat(topUser.label, is("zbyszkop"));
        assertThat(topUser.value, is(3));
    }

    @Test
    public void shouldCountThumbsForArticle() throws Exception {
        TermQuery articleFilterQuery = new TermQuery(new Term(FacetingIndexer.ARTICLE_FACET_FIELD, "Lucene best practices"));
        FacetResult topThumbsResult = getFacetResult(articleFilterQuery, FacetingIndexer.THUMB_FACET_FIELD);

        LabelAndValue[] labelValues = topThumbsResult.labelValues;
        assertThat(labelValues, is(arrayWithSize(1)));

        LabelAndValue topThumbs = labelValues[0];
        assertThat(topThumbs.label, is(Thumb.UP.name()));
        assertThat(topThumbs.value, is(2));
    }

    private FacetResult getFacetResult(Query query, String facetFieldName) throws IOException {
        FacetsCollector facetsCollector = new FacetsCollector();
        FacetsCollector.search(searcher, query, 0, facetsCollector);
        Facets facets = new FastTaxonomyFacetCounts(taxonomyReader, FacetingIndexer.FACETS_CONFIG, facetsCollector);
        return facets.getTopChildren(TOP_N_LIMIT, facetFieldName);
    }
}