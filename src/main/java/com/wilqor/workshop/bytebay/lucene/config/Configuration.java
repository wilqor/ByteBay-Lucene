package com.wilqor.workshop.bytebay.lucene.config;

import lombok.*;

import java.util.Map;

@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Configuration {
    String rootDirectory;
    Map<IndexType, String> indexDirectories;
}
