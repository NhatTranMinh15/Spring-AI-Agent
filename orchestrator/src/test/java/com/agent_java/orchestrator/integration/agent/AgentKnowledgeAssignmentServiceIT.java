package com.agent_java.orchestrator.integration.agent;

import com.agent_java.orchestrator.entity.agent.Agent;
import com.agent_java.orchestrator.entity.agent.knowledge.AgentKnowledge;
import com.agent_java.orchestrator.integration.BaseIntegrationTest;
import com.agent_java.orchestrator.repository.AgentKnowledgeRepository;
import com.agent_java.orchestrator.repository.AgentRepository;
import com.agent_java.orchestrator.repository.KnowledgeChunkRepository;
import com.agent_java.orchestrator.service.AgentKnowledgeAssignmentService;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AgentKnowledgeAssignmentServiceIT extends BaseIntegrationTest {

    private AgentKnowledgeAssignmentService service;
    private AgentRepository agentRepo;
    private AgentKnowledgeRepository knowledgeRepo;
    private KnowledgeChunkRepository chunkRepository;

    @Autowired
    public AgentKnowledgeAssignmentServiceIT(AgentKnowledgeAssignmentService service, AgentRepository agentRepo, AgentKnowledgeRepository knowledgeRepo, KnowledgeChunkRepository chunkRepository) {
        this.service = service;
        this.agentRepo = agentRepo;
        this.knowledgeRepo = knowledgeRepo;
        this.chunkRepository = chunkRepository;
    }

    @BeforeEach
    void cleanup() {
        chunkRepository.deleteAllInBatch();
        knowledgeRepo.deleteAllInBatch();
    }

    @Test
    public void assign_and_unassign_knowledge() {
        var agent = agentRepo.save(new Agent("A1-" + UUID.randomUUID(), "M1"));
        var knowledge = knowledgeRepo.save(new AgentKnowledge("K1-" + UUID.randomUUID()));

        service.assignKnowledge(agent.getId(), knowledge.getId());
        var list = service.getKnowledge(agent.getId());
        assertEquals(1, list.size());

        service.unassignKnowledge(agent.getId(), knowledge.getId());
        var listAfter = service.getKnowledge(agent.getId());
        assertTrue(listAfter.isEmpty());
    }
}
