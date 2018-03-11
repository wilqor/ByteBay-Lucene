package com.wilqor.workshop.bytebay.lucene.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.util.Strings;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class ConfigLoader {
    private static final Logger LOGGER = LogManager.getLogger(ConfigLoader.class);
    public static ConfigLoader LOADER = new ConfigLoader("config.yaml");

    private final Configuration configuration;

    private ConfigLoader(String configFileName) {
        LOGGER.info("Loading configuration from resource file: {}", configFileName);
        try (InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(configFileName)) {
            Yaml yaml = new Yaml();
            configuration = yaml.loadAs(inputStream, Configuration.class);
            LOGGER.info("Successfully loaded configuration: " + configuration);
        } catch (Exception e) {
            LOGGER.warn("Could not load configuration", e);
            throw new RuntimeException(e);
        }
    }

    public Path getPathForIndex(IndexType indexType) {
        return Paths.get(getRootDirectory(configuration.getRootDirectory()), configuration.getLuceneDirectory(), configuration.getIndexDirectories().get(indexType));
    }

    private String getRootDirectory(String loadedRootDirectory) {
        return Strings.isEmpty(loadedRootDirectory) ? System.getProperty("user.home") : loadedRootDirectory;
    }

    public static void main(String[] args) {
        Stream.of(IndexType.values()).forEach(type -> LOGGER.info("{} -> {}", type, LOADER.getPathForIndex(type)));
    }
}
