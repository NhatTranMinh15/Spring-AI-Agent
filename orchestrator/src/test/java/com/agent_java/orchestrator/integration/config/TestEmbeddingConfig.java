package com.agent_java.orchestrator.integration.config;

import com.agent_java.orchestrator.config.VectorEmbeddingProperties;
import java.util.ArrayList;
import java.util.List;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.BatchingStrategy;
import org.springframework.ai.embedding.Embedding;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.EmbeddingOptions;
import org.springframework.ai.embedding.EmbeddingRequest;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

/**
 * Test configuration providing a fake embedding model for tests.
 * Uses the same embedding dimension as production.
 */
@TestConfiguration
public class TestEmbeddingConfig {

    private VectorEmbeddingProperties properties;

    @Bean
    @Primary
    public EmbeddingModel fakeEmbeddingModel() {
        return new EmbeddingModel() {
            private final int dims = properties.getEmbeddingDimension();

            private float[] fill(int size, float value) {
                var fs = new float[size];
                for (int i = 0, len = fs.length; i < len; i++) {
                    fs[i] = value;
                }
                return fs;
            }

            @Override
            public float[] embed(String text) {
                return fill(dims, 0.0f);
            }

            @Override
            public List<float[]> embed(List<String> texts) {
                List<float[]> result = new ArrayList<>();
                for (int i = 0; i < texts.size(); i++) {
                    result.add(fill(dims, 0.0f));
                }
                return result;
            }

            @Override
            public float[] embed(Document document) {
                return fill(dims, 0.0f);
            }

            @Override
            public List<float[]> embed(List<Document> documents, EmbeddingOptions options, BatchingStrategy batchingStrategy) {
                List<float[]> result = new ArrayList<>();
                for (int i = 0; i < documents.size(); i++) {
                    result.add(fill(dims, 0.0f));
                }
                return result;
            }

            @Override
            public EmbeddingResponse call(EmbeddingRequest request) {
                var instructions = request.getInstructions();
                List<Embedding> embeddings = new ArrayList<>(instructions.size());
                for (int i = 0; i < instructions.size(); i++) {
                    embeddings.add(new Embedding(fill(dims, 0.0f), i));
                }
                return new EmbeddingResponse(embeddings);
            }

        };
    }
}
