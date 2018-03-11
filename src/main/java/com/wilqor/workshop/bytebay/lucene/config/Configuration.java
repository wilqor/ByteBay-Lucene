package com.wilqor.workshop.bytebay.lucene.config;

import java.util.Map;
import java.util.Objects;

public class Configuration {
    String rootDirectory;
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

    @Override
    public String toString() {
        return "Configuration{" +
                "rootDirectory='" + rootDirectory + '\'' +
                ", indexDirectories=" + indexDirectories +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Configuration that = (Configuration) o;
        return Objects.equals(rootDirectory, that.rootDirectory) &&
                Objects.equals(indexDirectories, that.indexDirectories);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rootDirectory, indexDirectories);
    }
}
