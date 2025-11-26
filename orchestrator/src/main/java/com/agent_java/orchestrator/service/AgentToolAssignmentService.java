package com.agent_java.orchestrator.service;

import com.agent_java.orchestrator.entity.agent.AgentTool;
import com.agent_java.orchestrator.entity.agent.mapping.AgentToolMapping;
import com.agent_java.orchestrator.repository.AgentRepository;
import com.agent_java.orchestrator.repository.AgentToolMappingRepository;
import com.agent_java.orchestrator.repository.AgentToolRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AgentToolAssignmentService {

    private final AgentRepository agentRepository;
    private final AgentToolRepository toolRepository;
    private final AgentToolMappingRepository mappingRepository;

    @Autowired
    public AgentToolAssignmentService(AgentRepository agentRepository, AgentToolRepository toolRepository, AgentToolMappingRepository mappingRepository) {
        this.agentRepository = agentRepository;
        this.toolRepository = toolRepository;
        this.mappingRepository = mappingRepository;
    }

    @Transactional
    public void assignTool(UUID agentId, UUID toolId) {
        var agent = agentRepository.findById(agentId).orElse(null);
        var tool = toolRepository.findById(toolId).orElse(null);

        if (agent == null || tool == null) {
            StringBuilder sb = new StringBuilder();

            if (agent == null) {
                sb.append("Agent not found: ").append(agentId).append(". ");
            }
            if (tool == null) {
                sb.append("Tool not found: ").append(toolId).append(".");
            }
            throw new EntityNotFoundException(sb.toString().trim());
        }

        if (!tool.isActive()) {
            throw new IllegalArgumentException("Cannot assign inactive tool to agent");
        }
        boolean isExisted = mappingRepository.existsByAgentIdAndToolId(agentId, toolId);
        if (isExisted) {
            throw new IllegalArgumentException("Tool already assigned to this agent");
        }

        mappingRepository.save(AgentToolMapping.of(agent, tool));
    }

    @Transactional
    public void unassignTool(UUID agentId, UUID toolId) {
        mappingRepository.deleteByAgentIdAndToolId(agentId, toolId);
    }

    @Transactional(readOnly = true)
    public List<AgentTool> getTools(UUID agentId) {
        return mappingRepository.findByAgentId(agentId).stream().map((t) -> t.getTool()).toList();
    }
}
