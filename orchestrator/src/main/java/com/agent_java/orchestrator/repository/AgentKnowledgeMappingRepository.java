package com.agent_java.orchestrator.repository;

import com.agent_java.orchestrator.entity.agent.mapping.AgentKnowledgeMapping;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

@Repository
public interface AgentKnowledgeMappingRepository extends JpaRepository<AgentKnowledgeMapping, UUID> {

    List<AgentKnowledgeMapping> findByAgentId(UUID agentId);

    boolean existsByAgentIdAndKnowledgeId(UUID agentId, UUID knowledgeId);

    @Modifying
    void deleteByAgentIdAndKnowledgeId(UUID agentId, UUID knowledgeId);
}
