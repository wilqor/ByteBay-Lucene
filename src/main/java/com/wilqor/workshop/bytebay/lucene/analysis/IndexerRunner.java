package com.wilqor.workshop.bytebay.lucene.analysis;

import com.wilqor.workshop.bytebay.lucene.source.Source;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.function.Supplier;

public class IndexerRunner<T> {
    private static final Logger LOGGER = LogManager.getLogger(IndexerRunner.class);

    private final Path indexRootDirectory;
    private final Supplier<Indexer<T>> indexerSupplier;
    private final Source<T> dataSource;

    private IndexerRunner(Path indexRootDirectory, Supplier<Indexer<T>> indexerSupplier, Source<T> dataSource) {
        this.indexRootDirectory = indexRootDirectory;
        this.indexerSupplier = indexerSupplier;
        this.dataSource = dataSource;
    }

    public void runIndexer() throws Exception {
        if (Files.isDirectory(indexRootDirectory)) {
            LOGGER.info("Index root directory \"{}\" exists, proceeding to clean up");
            Files.walk(indexRootDirectory)
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
            LOGGER.info("Index root directory \"{}\" cleared");
        }
        try (Indexer<T> indexer = indexerSupplier.get()) {
            LOGGER.info("Starting indexing...");
            indexer.index(dataSource);
            LOGGER.info("Indexing complete");
        }
    }

    static <T> IndexerRunner<T> of(Path indexRootDirectory, Supplier<Indexer<T>> indexerSupplier, Source<T> dataSource) {
        return new IndexerRunner<>(indexRootDirectory, indexerSupplier, dataSource);
    }
}
