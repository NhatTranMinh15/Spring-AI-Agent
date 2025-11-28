package com.agent_java.orchestrator.service;

import com.agent_java.orchestrator.dto.KnowledgeChunkResponseDto;
import com.agent_java.orchestrator.entity.agent.knowledge.KnowledgeChunk;
import com.agent_java.orchestrator.repository.AgentKnowledgeRepository;
import com.agent_java.orchestrator.repository.KnowledgeChunkRepository;
import com.agent_java.orchestrator.utils.Constant;
import jakarta.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.ai.document.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class KnowledgeChunkService {

    private final KnowledgeChunkRepository chunkRepo;
    private final AgentKnowledgeRepository knowledgeRepo;
    private final VectorStoreService vectorStoreService;
    private final DynamicModelService dynamicModelService;

    @Autowired
    public KnowledgeChunkService(KnowledgeChunkRepository chunkRepo, AgentKnowledgeRepository knowledgeRepo, VectorStoreService vectorStoreService, DynamicModelService dynamicModelService) {
        this.chunkRepo = chunkRepo;
        this.knowledgeRepo = knowledgeRepo;
        this.vectorStoreService = vectorStoreService;
        this.dynamicModelService = dynamicModelService;
    }

    @Transactional
    public KnowledgeChunkResponseDto addChunk(UUID agentId, UUID knowledgeId, String content, Map<String, Object> metadata, Integer chunkOrder) {
        var knowledge = knowledgeRepo.findByIdAndAgentId(knowledgeId, agentId)
                .orElseThrow(() -> new EntityNotFoundException("Knowledge " + knowledgeId + " not found for agent " + agentId));

        var order = chunkOrder != null ? chunkOrder : getNextChunkOrderForKnowledge(agentId, knowledgeId);
        var embedding = dynamicModelService.getEmbeddingModel(agentId).embed(content);

        var chunk = new KnowledgeChunk(
                knowledge,
                order,
                content,
                metadata,
                embedding.length == Constant.GEMINI_DIMENSION ? embedding : null,
                embedding.length == Constant.CHATGPT_DIMENSION ? embedding : null);

        var savedChunk = chunkRepo.save(chunk);
        vectorStoreService.getVectorStore(agentId).add(List.of(buildDocument(savedChunk)));
        return KnowledgeChunkResponseDto.from(savedChunk);
    }

    @Transactional
    public KnowledgeChunkResponseDto updateChunk(UUID agentId, UUID knowledgeId, UUID chunkId, String newContent, Map<String, Object> newMetadata) {
        var chunk = chunkRepo.findById(chunkId)
                .orElseThrow(() -> new EntityNotFoundException("Chunk " + chunkId + " not found"));

        if (chunk.getKnowledge().getId() != knowledgeId) {
            throw new IllegalArgumentException("Chunk " + chunkId + " does not belong to knowledge " + knowledgeId);
        }

        if (chunk.getKnowledge().getAgent().getId() != agentId) {
            throw new EntityNotFoundException("Chunk $chunkId does not belong to agent $agentId");
        }

        var embedding = dynamicModelService.getEmbeddingModel(agentId).embed(newContent);
        chunk.setContent(newContent);
        chunk.setMetadata(newMetadata);
        switch (embedding.length) {
            case Constant.GEMINI_DIMENSION -> {
                chunk.setEmbedding768(embedding);
            }
            case Constant.CHATGPT_DIMENSION -> {
                chunk.setEmbedding1536(embedding);
            }
            default ->
                throw new IllegalArgumentException("Unsupported embedding dimension: " + embedding.length);
        }
        vectorStoreService.getVectorStore(agentId).add(List.of(buildDocument(chunk)));
        return KnowledgeChunkResponseDto.from(chunkRepo.save(chunk));
    }

    @Transactional(readOnly = true)
    public List<KnowledgeChunkResponseDto> getByKnowledge(UUID agentId, UUID knowledgeId) {
        return chunkRepo.findAllByKnowledgeIdAndKnowledgeAgentIdOrderByChunkOrderAsc(knowledgeId, agentId)
                .stream()
                .map(KnowledgeChunkResponseDto::from)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public long countByKnowledge(UUID agentId, UUID knowledgeId) {
        return chunkRepo.countByKnowledgeIdAndKnowledgeAgentId(knowledgeId, agentId);
    }

    @Transactional(readOnly = true)
    public int getNextChunkOrderForKnowledge(UUID agentId, UUID knowledgeId) {
        var currentOrder = chunkRepo.findMaxChunkOrderByKnowledgeIdAndAgentId(knowledgeId, agentId).orElse(0);
        return currentOrder + 1;
    }

    @Transactional(readOnly = true)
    public List<KnowledgeChunkResponseDto> searchSimilarChunks(UUID agentId, UUID knowledgeId, String query, Integer topK) {
        // Ensure knowledge belongs to agent

        if (!knowledgeRepo.existsByIdAndAgentId(knowledgeId, agentId)) {
            throw new EntityNotFoundException("Knowledge " + knowledgeId + " not found for agent " + agentId);
        }

        List<Document> results = vectorStoreService.getVectorStore(agentId).similaritySearch(query);

        if (results.isEmpty()) {
            return new ArrayList<>();
        }

        var activeKnowledgeIds = chunkRepo.findAllKnowledgeIdsActiveByAgent(agentId).stream().map((t) -> t.toString()).collect(Collectors.toList());

        return results.stream()
                .filter((doc) -> {
                    var knowledgeId1 = doc.getMetadata().get("knowledge_id");
                    return knowledgeId1 != null && activeKnowledgeIds.contains(knowledgeId1.toString());
                })
                .limit(topK)
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
