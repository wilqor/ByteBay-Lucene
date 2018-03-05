package com.wilqor.workshop.bytebay.lucene.source.model;


import lombok.*;

@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class SimpleReview {
    protected String userName;
    protected Thumb thumb;
    protected String articleName;
}
