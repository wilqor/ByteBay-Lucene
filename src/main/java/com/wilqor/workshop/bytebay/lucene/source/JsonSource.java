package com.wilqor.workshop.bytebay.lucene.source;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wilqor.workshop.bytebay.lucene.source.model.WikipediaPage;
import org.apache.commons.compress.compressors.CompressorException;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JsonSource<T> implements Source<T> {
    private static final Logger LOGGER = LoggerFactory.getLogger(JsonSource.class);

    private final String fileName;
    private final Class<T> entityClass;

    JsonSource(String fileName, Class<T> entityClass) {
        this.fileName = fileName;
        this.entityClass = entityClass;
    }

    @Override
    public Stream<T> stream() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {

            Path dataSource = Paths.get(Thread.currentThread().getContextClassLoader().getResource(fileName).toURI());

            Stream<String> lines = fileName.endsWith(".bz2") ?
                    getBufferedReaderForCompressedFile(dataSource.toFile()).lines() :
                    Files.lines(dataSource);

            return lines
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

    private BufferedReader getBufferedReaderForCompressedFile(File fileIn) throws FileNotFoundException, CompressorException {
        FileInputStream fin = new FileInputStream(fileIn);
        BufferedInputStream bis = new BufferedInputStream(fin);
        CompressorInputStream input = new CompressorStreamFactory().createCompressorInputStream(bis);
        BufferedReader br2 = new BufferedReader(new InputStreamReader(input));
        return br2;
    }

    public static void main(String[] args) {
        List<WikipediaPage> reviews = Source.WIKIPEDIA_PAGE_MODEL.stream().limit(10).collect(Collectors.toList());

        reviews.forEach(review -> LOGGER.info("Loaded review: {}", review));
    }
}
