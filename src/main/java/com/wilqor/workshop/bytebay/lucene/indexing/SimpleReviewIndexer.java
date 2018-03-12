package com.wilqor.workshop.bytebay.lucene.indexing;

import com.wilqor.workshop.bytebay.lucene.source.model.SimpleReview;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;

import java.io.IOException;
import java.nio.file.Path;

public class SimpleReviewIndexer extends BaseIndexer<SimpleReview> {
    public SimpleReviewIndexer(Path targetDirectory, Analyzer analyzer) throws IOException {
        super(targetDirectory, analyzer);
    }

    @Override
    protected Document mapToDocument(SimpleReview sourceItem) {
        Document reviewDocument = new Document();
        reviewDocument.add(new StringField(SimpleReview.USER_NAME_FIELD, sourceItem.getUserName(), Field.Store.YES));
        reviewDocument.add(new StringField(SimpleReview.THUMB_FIELD, sourceItem.getThumb().name(), Field.Store.YES));
        reviewDocument.add(new TextField(SimpleReview.ARTICLE_NAME_FIELD, sourceItem.getArticleName(), Field.Store.YES));
        return reviewDocument;
    }
}
