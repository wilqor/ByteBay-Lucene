package com.wilqor.workshop.bytebay.lucene.wikipediasearch;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
class SearchResultEntry {
    private String title;
    private String link;
    private String description;

}
