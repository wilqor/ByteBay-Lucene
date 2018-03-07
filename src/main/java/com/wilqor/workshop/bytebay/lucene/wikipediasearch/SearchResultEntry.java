package com.wilqor.workshop.bytebay.lucene.wikipediasearch;


import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
class SearchResultEntry {
    private String title;
    private String link;
    private String description;

}
