package com.agent_java.orchestrator.service;

import com.agent_java.orchestrator.dto.KnowledgeChunkResponseDto;
import com.agent_java.orchestrator.entity.agent.knowledge.KnowledgeChunk;
import com.agent_java.orchestrator.repository.AgentKnowledgeRepository;
import com.agent_java.orchestrator.repository.KnowledgeChunkRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class KnowledgeChunkService {

    private final KnowledgeChunkRepository chunkRepo;
    private final AgentKnowledgeRepository knowledgeRepo;
    private final VectorStore vectorStore;
    private final EmbeddingModel embeddingModel;

    @Autowired
    public KnowledgeChunkService(KnowledgeChunkRepository chunkRepo, AgentKnowledgeRepository knowledgeRepo, VectorStore vectorStore, EmbeddingModel embeddingModel) {
        this.chunkRepo = chunkRepo;
        this.knowledgeRepo = knowledgeRepo;
        this.vectorStore = vectorStore;
        this.embeddingModel = embeddingModel;
    }

    @Transactional
    public KnowledgeChunkResponseDto addChunk(UUID knowledgeId, String content, Map<String, Object> metadata, Integer chunkOrder) {
        var knowledge = knowledgeRepo.findById(knowledgeId).orElseThrow(() -> new EntityNotFoundException("Knowledge not found: " + knowledgeId));

        var order = chunkOrder != null ? chunkOrder : getNextChunkOrderForKnowledge(knowledgeId);
        var embedding = embeddingModel.embed(content);

        var chunk = new KnowledgeChunk(knowledge, order, content, metadata, embedding);
        var savedChunk = chunkRepo.save(chunk);
        vectorStore.add(List.of(buildDocument(savedChunk)));
        return KnowledgeChunkResponseDto.from(savedChunk);
    }

    @Transactional
    public KnowledgeChunkResponseDto updateChunk(UUID chunkId, String newContent, Map<String, Object> newMetadata) {
        var chunk
                = chunkRepo.findById(chunkId).orElseThrow(() -> new EntityNotFoundException("Chunk not found: " + chunkId));

        chunk.setContent(newContent);
        chunk.setMetadata(newMetadata);
        chunk.setEmbedding(embeddingModel.embed(newContent));

        vectorStore.add(List.of(buildDocument(chunk)));
        return KnowledgeChunkResponseDto.from(chunk);
    }

    @Transactional(readOnly = true)
    public List<KnowledgeChunkResponseDto> getByKnowledge(UUID knowledgeId) {
        return chunkRepo.findAllByKnowledgeId(knowledgeId).stream().map(KnowledgeChunkResponseDto::from).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public long countByKnowledge(UUID knowledgeId) {
        return chunkRepo.countByKnowledgeId(knowledgeId);
    }

    @Transactional(readOnly = true)
    public Integer getNextChunkOrderForKnowledge(UUID knowledgeId) {
        var currentOrder = chunkRepo.findMaxChunkOrderByKnowledgeId(knowledgeId).orElse(0);
        return currentOrder + 1;
    }

    @Transactional(readOnly = true)
    public List<KnowledgeChunkResponseDto> searchSimilarChunks(String query, Integer topK) {
        var activeKnowledgeIds = chunkRepo.findAllKnowledgeIdsActive().stream().map((t) -> t.toString()).collect(Collectors.toList());

        List<Document> results = vectorStore.similaritySearch(query);

        return results.stream()
                .filter((doc) -> {
                    var knowledgeId = doc.getMetadata().get("knowledge_id");
                    return knowledgeId != null && activeKnowledgeIds.contains(knowledgeId.toString());
                })
                .map(KnowledgeChunkResponseDto::fromDocument)
                .collect(Collectors.toList());
    }

    private Document buildDocument(KnowledgeChunk chunk) {
        Map<String, Object> metadata = new HashMap<>(chunk.getMetadata());
        metadata.put("chunk_id", chunk.getId().toString());
        metadata.put("knowledge_id", chunk.getKnowledge().getId().toString());
        metadata.put("chunk_order", chunk.getChunkOrder());
        return new Document(chunk.getId().toString(), chunk.getContent(), metadata);
    }
}
