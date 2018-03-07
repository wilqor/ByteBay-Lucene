package com.wilqor.workshop.bytebay.lucene.source.model;


import lombok.*;

@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class JsonWikipediaPage {
    private String id;
    private String title;
    private String url;
    private String text;

}
