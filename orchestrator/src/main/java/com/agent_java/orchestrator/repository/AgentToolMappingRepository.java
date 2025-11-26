package com.agent_java.orchestrator.repository;

import com.agent_java.orchestrator.entity.agent.mapping.AgentToolMapping;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

@Repository
public interface AgentToolMappingRepository extends JpaRepository<AgentToolMapping, UUID> {

    List<AgentToolMapping> findByAgentId(UUID agentId);

    boolean existsByAgentIdAndToolId(UUID agentId, UUID toolId);

    @Modifying
    void deleteByAgentIdAndToolId(UUID agentId, UUID toolId);
}
