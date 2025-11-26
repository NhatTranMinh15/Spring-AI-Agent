package com.agent_java.orchestrator.service;

import com.agent_java.orchestrator.entity.agent.knowledge.AgentKnowledge;
import com.agent_java.orchestrator.entity.agent.mapping.AgentKnowledgeMapping;
import com.agent_java.orchestrator.repository.AgentKnowledgeMappingRepository;
import com.agent_java.orchestrator.repository.AgentKnowledgeRepository;
import com.agent_java.orchestrator.repository.AgentRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AgentKnowledgeAssignmentService {

    AgentRepository agentRepository;
    AgentKnowledgeRepository knowledgeRepository;
    AgentKnowledgeMappingRepository mappingRepository;

    @Autowired
    public AgentKnowledgeAssignmentService(AgentRepository agentRepository, AgentKnowledgeRepository knowledgeRepository, AgentKnowledgeMappingRepository mappingRepository) {
        this.agentRepository = agentRepository;
        this.knowledgeRepository = knowledgeRepository;
        this.mappingRepository = mappingRepository;
    }

    @Transactional
    public void assignKnowledge(UUID agentId, UUID knowledgeId) {
        var agent = agentRepository.findById(agentId).orElse(null);
        var knowledge = knowledgeRepository.findById(knowledgeId).orElse(null);
        if (agent == null || knowledge == null) {
            StringBuilder sb = new StringBuilder();
            if (agent == null) {
                sb.append("Agent not found: ").append(agentId).append(". ");
            }
            if (knowledge == null) {
                sb.append("Knowledge not found: ").append(knowledgeId).append(".");
            }
            throw new EntityNotFoundException(sb.toString().trim());
        }
        if (!knowledge.isActive()) {
            throw new IllegalArgumentException("Knowledge is inactive and cannot be assigned");
        }
        boolean mappingExist = mappingRepository.existsByAgentIdAndKnowledgeId(agentId, knowledgeId);
        if (mappingExist) {
            throw new IllegalArgumentException("Knowledge already assigned to this agent");
        }
        mappingRepository.save(AgentKnowledgeMapping.of(agent, knowledge));
    }

    @Transactional
    public void unassignKnowledge(UUID agentId, UUID knowledgeId) {
        mappingRepository.deleteByAgentIdAndKnowledgeId(agentId, knowledgeId);
    }

    @Transactional(readOnly = true)
    public List<AgentKnowledge> getKnowledge(UUID agentId) {
        return mappingRepository.findByAgentId(agentId).stream().map((t) -> t.getKnowledge()).toList();
    }
}
