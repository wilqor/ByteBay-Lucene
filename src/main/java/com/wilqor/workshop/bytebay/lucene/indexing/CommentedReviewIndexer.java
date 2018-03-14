package com.wilqor.workshop.bytebay.lucene.indexing;

import com.wilqor.workshop.bytebay.lucene.source.model.CommentedReview;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;

import java.io.IOException;
import java.nio.file.Path;

// TODO implement indexer, index user name and thumb as string, index article name and comment as text
public class CommentedReviewIndexer extends BaseIndexer<CommentedReview> {
    public CommentedReviewIndexer(Path targetDirectory, Analyzer analyzer) throws IOException {
        super(targetDirectory, analyzer);
    }

    @Override
    protected Document mapToDocument(CommentedReview sourceItem) {
        throw new UnsupportedOperationException("Mapping documents in CommentedReviewIndexer not yet implemented!");
    }
}
