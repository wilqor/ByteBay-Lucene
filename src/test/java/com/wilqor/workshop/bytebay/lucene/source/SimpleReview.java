package com.wilqor.workshop.bytebay.lucene.source;


import lombok.*;

@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class SimpleReview {

    String userName;
    Thumb thumb;
    String articleName;
}
