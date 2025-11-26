package com.agent_java.orchestrator.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "spring.ai.vectorstore.pgvector")
public class VectorEmbeddingProperties {

    private static final int DEFAULT_EMBEDDING_DIMENSION = 1536;
    
    int embeddingDimension = DEFAULT_EMBEDDING_DIMENSION;

    public int getEmbeddingDimension() {
        return embeddingDimension;
    }
    
}
