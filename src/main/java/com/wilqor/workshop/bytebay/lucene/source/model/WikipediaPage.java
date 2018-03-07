package com.wilqor.workshop.bytebay.lucene.source.model;

import lombok.*;

import java.util.Scanner;


@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class WikipediaPage extends JsonWikipediaPage {

    private String description;

    public static WikipediaPage fromJsonWikipediaPage(JsonWikipediaPage page) {

        Scanner scanner = new Scanner(page.getText());
        scanner.nextLine(); //title line
        String description = "";
        while (scanner.hasNext() && (description = scanner.nextLine()).length() == 0 );
        WikipediaPage wikipediaPage = new WikipediaPage(description);
        wikipediaPage.setId(page.getId());
        wikipediaPage.setText(page.getText());
        wikipediaPage.setTitle(page.getTitle());
        wikipediaPage.setUrl(page.getUrl());

        return wikipediaPage;

    }
}
