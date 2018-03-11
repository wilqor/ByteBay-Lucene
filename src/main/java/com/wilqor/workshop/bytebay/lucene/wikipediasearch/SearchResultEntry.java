package com.wilqor.workshop.bytebay.lucene.wikipediasearch;

class SearchResultEntry {
    private String title;
    private String link;
    private String description;

    public SearchResultEntry() {
    }

    public SearchResultEntry(String title, String link, String description) {
        this.title = title;
        this.link = link;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "SearchResultEntry{" +
                "title='" + title + '\'' +
                ", link='" + link + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String title;
        private String link;
        private String description;

        private Builder() {
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder link(String link) {
            this.link = link;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public SearchResultEntry build() {
            SearchResultEntry searchResultEntry = new SearchResultEntry();
            searchResultEntry.setTitle(title);
            searchResultEntry.setLink(link);
            searchResultEntry.setDescription(description);
            return searchResultEntry;
        }
    }
}
