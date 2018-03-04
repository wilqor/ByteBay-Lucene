package com.wilqor.workshop.bytebay.lucene.source;


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
