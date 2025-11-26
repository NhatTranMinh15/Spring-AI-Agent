package com.agent_java.orchestrator.config;

import java.util.Map;
import org.flywaydb.core.api.configuration.FluentConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.autoconfigure.flyway.FlywayConfigurationCustomizer;
import org.springframework.context.annotation.Bean;

@Configuration
public class FlywayPlaceholderSyncConfig {

    private final VectorEmbeddingProperties properties;

    @Autowired
    public FlywayPlaceholderSyncConfig(VectorEmbeddingProperties properties) {
        this.properties = properties;
    }

    @Bean
    public FlywayConfigurationCustomizer flywayCustomizer() {
        return (FluentConfiguration config) -> {
            config.placeholders(Map.of("spring.ai.vectorstore.pgvector.embedding-dimension", Integer.toString(properties.getEmbeddingDimension())));
        };
    }
}
