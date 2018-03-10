package com.wilqor.workshop.bytebay.lucene.source.model;

import java.util.Scanner;


public class WikipediaPage extends JsonWikipediaPage {

    private String description;

    @java.beans.ConstructorProperties({"description"})
    private WikipediaPage(String description) {
        this.description = description;
    }

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

    public String getDescription() {
        return this.description;
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof WikipediaPage)) return false;
        final WikipediaPage other = (WikipediaPage) o;
        if (!other.canEqual((Object) this)) return false;
        if (!super.equals(o)) return false;
        final Object this$description = this.getDescription();
        final Object other$description = other.getDescription();
        if (this$description == null ? other$description != null : !this$description.equals(other$description))
            return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * PRIME + super.hashCode();
        final Object $description = this.getDescription();
        result = result * PRIME + ($description == null ? 43 : $description.hashCode());
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof WikipediaPage;
    }

    public String toString() {
        return "WikipediaPage(super=" + super.toString() + ", description=" + this.getDescription() + ")";
    }
}
