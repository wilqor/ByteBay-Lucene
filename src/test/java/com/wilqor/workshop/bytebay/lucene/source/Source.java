package com.wilqor.workshop.bytebay.lucene.source;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class Source<T> {

    public static Source<SimpleReview> SIMPLE_MODEL = new Source<>("simple_model.json.batch", SimpleReview.class);

    private final String fileName;
    private Class<T> entityClass;


    Source(String fileName, Class<T> entityClass) {
        this.fileName = fileName;

        this.entityClass = entityClass;
    }

    public Stream<T> stream() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {

            Path dataSource = Paths.get(Thread.currentThread().getContextClassLoader().getResource(fileName).toURI());
            return Files.lines(dataSource)
                    .map(line -> {
                        try {
                            return objectMapper.readValue(line, entityClass);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public static void main(String[] args) {
        Source.SIMPLE_MODEL.stream()
                .forEach(System.out::println);
    }
}



