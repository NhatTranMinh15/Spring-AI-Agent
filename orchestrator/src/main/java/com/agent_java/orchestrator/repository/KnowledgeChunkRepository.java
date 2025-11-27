package com.agent_java.orchestrator.repository;

import com.agent_java.orchestrator.entity.agent.knowledge.KnowledgeChunk;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface KnowledgeChunkRepository extends JpaRepository<KnowledgeChunk, UUID> {

    /**
     * Get all chunks for a knowledge source belonging to a specific agent
     *
     * @param knowledgeId
     * @param agentId
     * @return List of KnowledgeChunk
     */
    public List<KnowledgeChunk> findAllByKnowledgeIdAndKnowledgeAgentIdOrderByChunkOrderAsc(UUID knowledgeId, UUID agentId);

    /**
     * Count chunks for a knowledge source of a specific agent
     *
     * @param knowledgeId
     * @param agentId
     * @return total number of chunks for a knowledge source of a specific agent
     */
    public long countByKnowledgeIdAndKnowledgeAgentId(UUID knowledgeId, UUID agentId);

    /**
     * Find a chunk by its ID, knowledge ID, and agent ID
     *
     * @param chunkId
     * @param knowledgeId
     * @param agentId
     * @return A KnowledgeChunk, optional
     */
    public Optional<KnowledgeChunk> findByIdAndKnowledgeIdAndKnowledgeAgentId(UUID chunkId, UUID knowledgeId, UUID agentId);

    /**
     * Get the max chunk order for a knowledge source of a specific agent
     *
     * @param knowledgeId
     * @param agentId
     * @return the max chunk order, optional
     */
    @Query("SELECT MAX(c.chunkOrder) FROM KnowledgeChunk c WHERE c.knowledge.id = :knowledgeId AND c.knowledge.agent.id = :agentId")
    public Optional<Integer> findMaxChunkOrderByKnowledgeIdAndAgentId(UUID knowledgeId, UUID agentId);

    /**
     * Get IDs of all active knowledge for a specific agent
     *
     * @param agentId
     * @return List of UUID
     */
    @Query("SELECT c.knowledge.id FROM KnowledgeChunk c WHERE c.knowledge.active = true AND c.knowledge.agent.id = :agentId")
    public List<UUID> findAllKnowledgeIdsActiveByAgent(UUID agentId);
}
