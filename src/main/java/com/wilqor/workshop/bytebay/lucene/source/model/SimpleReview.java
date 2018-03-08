package com.wilqor.workshop.bytebay.lucene.source.model;


import lombok.*;

@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class SimpleReview {
    public static final String USER_NAME_FIELD = "user_name";
    public static final String THUMB_FIELD = "thumb";
    public static final String ARTICLE_NAME_FIELD = "article_name";

    protected String userName;
    protected Thumb thumb;
    protected String articleName;
}
