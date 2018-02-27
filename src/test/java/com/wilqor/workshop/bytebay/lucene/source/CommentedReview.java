package com.wilqor.workshop.bytebay.lucene.source;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode
public class CommentedReview extends SimpleReview {
    String content;
}
