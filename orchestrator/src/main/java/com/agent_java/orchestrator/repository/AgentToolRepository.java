package com.agent_java.orchestrator.repository;

import com.agent_java.orchestrator.entity.agent.AgentTool;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

@Repository
public interface AgentToolRepository extends JpaRepository<AgentTool, UUID> {

    public List<AgentTool> findByAgentId(UUID agentId);

    public boolean existsByAgentIdAndToolId(UUID agentId, UUID toolId);

    @Modifying
    public void deleteByAgentIdAndToolId(UUID agentId, UUID toolId);
}
