package com.wilqor.workshop.bytebay.lucene.analysis;

import com.wilqor.workshop.bytebay.lucene.source.model.CommentedReview;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

public class CommentedReviewIndexer extends BaseIndexer<CommentedReview> {
    static final String USER_NAME_FIELD = "user_name";
    static final String THUMB_FIELD = "thumb";
    static final String ARTICLE_NAME_FIELD = "article_name";
    static final String COMMENT_FIELD = "comment";

    CommentedReviewIndexer(Path targetDirectory, Analyzer analyzer) throws IOException {
        super(targetDirectory, analyzer);
    }

    @Override
    protected Document mapToDocument(CommentedReview sourceItem) {
        Document reviewDocument = new Document();
        reviewDocument.add(new StringField(USER_NAME_FIELD, sourceItem.getUserName(), Field.Store.YES));
        reviewDocument.add(new StringField(THUMB_FIELD, sourceItem.getThumb().name(), Field.Store.YES));
        reviewDocument.add(new TextField(ARTICLE_NAME_FIELD, sourceItem.getArticleName(), Field.Store.YES));
        Optional.ofNullable(sourceItem.getComment())
                .ifPresent(comment -> reviewDocument.add(new TextField(COMMENT_FIELD, comment, Field.Store.YES)));
        return reviewDocument;
    }
}
