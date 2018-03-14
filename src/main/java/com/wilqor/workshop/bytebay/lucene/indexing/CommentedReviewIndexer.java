package com.wilqor.workshop.bytebay.lucene.indexing;

import com.wilqor.workshop.bytebay.lucene.source.model.CommentedReview;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

// TODO implement indexer, index user name and thumb as string, index article name and comment as text
public class CommentedReviewIndexer extends BaseIndexer<CommentedReview> {
    public CommentedReviewIndexer(Path targetDirectory, Analyzer analyzer) throws IOException {
        super(targetDirectory, analyzer);
    }

    @Override
    protected Document mapToDocument(CommentedReview sourceItem) {
        Document document = new Document();
        document.add(new StringField(CommentedReview.USER_NAME_FIELD, sourceItem.getUserName(), Field.Store.YES));
        document.add(new StringField(CommentedReview.THUMB_FIELD, sourceItem.getThumb().name(), Field.Store.YES));
        document.add(new TextField(CommentedReview.ARTICLE_NAME_FIELD, sourceItem.getArticleName(), Field.Store.YES));
        Optional.ofNullable(sourceItem.getComment()).ifPresent(comment -> document.add(new TextField(CommentedReview.COMMENT_FIELD, comment, Field.Store.YES)));
        return document;
    }
}
