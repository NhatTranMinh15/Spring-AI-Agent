package com.agent_java.orchestrator.service;

import java.util.UUID;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class VectorStoreService {

    private final DynamicModelService dynamicModelService;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public VectorStoreService(DynamicModelService dynamicModelService, JdbcTemplate jdbcTemplate) {
        this.dynamicModelService = dynamicModelService;
        this.jdbcTemplate = jdbcTemplate;
    }

    public PgVectorStore getVectorStore(UUID agentId) {
        var embeddingModel = dynamicModelService.getEmbeddingModel(agentId);
        var dimension = embeddingModel.dimensions();
        return PgVectorStore
                .builder(jdbcTemplate, embeddingModel)
                .dimensions(dimension)
                .schemaName("public")
                .vectorTableName("vector_store_" + dimension)
                .build();
    }

}
