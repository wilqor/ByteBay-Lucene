package com.wilqor.workshop.bytebay.lucene.source.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class CommentedReviewWithTimestamp extends CommentedReview {

    public static String TIMESTAMP_FIELD = "timestamp";
    long timestamp;
}
