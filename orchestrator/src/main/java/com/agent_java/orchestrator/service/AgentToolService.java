package com.agent_java.orchestrator.service;

import com.agent_java.orchestrator.dto.AgentToolResponseDto;
import com.agent_java.orchestrator.entity.agent.AgentTool;
import com.agent_java.orchestrator.mapper.AgentToolMapper;
import com.agent_java.orchestrator.repository.AgentRepository;
import com.agent_java.orchestrator.repository.AgentToolRepository;
import com.agent_java.orchestrator.repository.ToolRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AgentToolService {

    private final AgentRepository agentRepository;
    private final ToolRepository toolRepository;
    private final AgentToolRepository agentToolRepository;

    @Autowired
    public AgentToolService(AgentRepository agentRepository, ToolRepository toolRepository, AgentToolRepository agentToolRepository) {
        this.agentRepository = agentRepository;
        this.toolRepository = toolRepository;
        this.agentToolRepository = agentToolRepository;
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
        boolean isExisted = agentToolRepository.existsByAgentIdAndToolId(agentId, toolId);
        if (isExisted) {
            throw new IllegalArgumentException("Tool already assigned to this agent");
        }

        agentToolRepository.save(AgentTool.of(agent, tool));
    }

    @Transactional
    public void unassignTool(UUID agentId, UUID toolId) {
        agentToolRepository.deleteByAgentIdAndToolId(agentId, toolId);
    }

    @Transactional(readOnly = true)
    public List<AgentToolResponseDto> getTools(UUID agentId) {
        return agentToolRepository.findByAgentId(agentId).stream().map(AgentToolMapper::toResponse).toList();
    }
}
