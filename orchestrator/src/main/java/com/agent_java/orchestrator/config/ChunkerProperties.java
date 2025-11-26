package com.agent_java.orchestrator.config;

import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "chunker")
@Getter
public class ChunkerProperties {

    private static final int DEFAULT_CHUNK_SIZE = 500;
    private static final int DEFAULT_MIN_CHUNK_SIZE_CHARS = 300;
    private static final int DEFAULT_MIN_CHUNK_LENGTH_TO_EMBED = 10;
    private static final int DEFAULT_MAX_NUM_CHUNKS = 1000;

    Map<String, ChunkerProfile> profiles = new HashMap<>();

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ChunkerProfile {

        private int chunkSize = DEFAULT_CHUNK_SIZE;
        private int minChunkSizeChars = DEFAULT_MIN_CHUNK_SIZE_CHARS;
        private int minChunkLengthToEmbed = DEFAULT_MIN_CHUNK_LENGTH_TO_EMBED;
        private int maxNumChunks = DEFAULT_MAX_NUM_CHUNKS;
        private boolean keepSeparator = true;
    }
}
