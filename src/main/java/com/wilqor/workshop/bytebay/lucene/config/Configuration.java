package com.wilqor.workshop.bytebay.lucene.config;

import java.util.Map;
import java.util.Objects;

public class Configuration {
    String rootDirectory;
    String luceneDirectory;
    Map<IndexType, String> indexDirectories;

    public Configuration() {

    }

    public String getRootDirectory() {
        return rootDirectory;
    }

    public void setRootDirectory(String rootDirectory) {
        this.rootDirectory = rootDirectory;
    }

    public Map<IndexType, String> getIndexDirectories() {
        return indexDirectories;
    }

    public void setIndexDirectories(Map<IndexType, String> indexDirectories) {
        this.indexDirectories = indexDirectories;
    }

    public String getLuceneDirectory() {
        return luceneDirectory;
    }

    public void setLuceneDirectory(String luceneDirectory) {
        this.luceneDirectory = luceneDirectory;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Configuration that = (Configuration) o;
        return Objects.equals(rootDirectory, that.rootDirectory) &&
                Objects.equals(luceneDirectory, that.luceneDirectory) &&
                Objects.equals(indexDirectories, that.indexDirectories);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rootDirectory, luceneDirectory, indexDirectories);
    }

    @Override
    public String toString() {
        return "Configuration{" +
                "rootDirectory='" + rootDirectory + '\'' +
                ", luceneDirectory='" + luceneDirectory + '\'' +
                ", indexDirectories=" + indexDirectories +
                '}';
    }

}
