package com.agent_java.orchestrator.repository;

import com.agent_java.orchestrator.entity.agent.knowledge.AgentKnowledge;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgentKnowledgeRepository extends JpaRepository<AgentKnowledge, UUID> {

    List<AgentKnowledge> findAllByActiveTrue();
}
