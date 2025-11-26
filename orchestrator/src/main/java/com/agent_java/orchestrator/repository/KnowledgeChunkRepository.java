package com.agent_java.orchestrator.repository;

import com.agent_java.orchestrator.entity.agent.knowledge.KnowledgeChunk;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository

public interface KnowledgeChunkRepository extends JpaRepository<KnowledgeChunk, UUID> {

    List<KnowledgeChunk> findAllByKnowledgeId(UUID knowledgeId);

    List<KnowledgeChunk> findAllByKnowledgeIdOrderByChunkOrderAsc(UUID knowledgeId);

    long countByKnowledgeId(UUID knowledgeId);

    /**
     * Custom query to get IDs of all chunks whose knowledge is active
     *
     * @return list of active knowledge UUID
     */
    @Query("SELECT c.knowledge.id FROM KnowledgeChunk c WHERE c.knowledge.active = true")
    List<UUID> findAllKnowledgeIdsActive();

    @Query("SELECT MAX(c.chunkOrder) FROM KnowledgeChunk c WHERE c.knowledge.id = :knowledgeId")
    Optional<Integer> findMaxChunkOrderByKnowledgeId(@Param("knowledgeId") UUID knowledgeId);
}
