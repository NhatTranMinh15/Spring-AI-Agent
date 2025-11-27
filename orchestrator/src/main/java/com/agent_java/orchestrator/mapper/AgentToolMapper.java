package com.agent_java.orchestrator.mapper;

import com.agent_java.orchestrator.dto.AgentToolResponseDto;
import com.agent_java.orchestrator.entity.Tool;
import com.agent_java.orchestrator.entity.agent.Agent;
import com.agent_java.orchestrator.entity.agent.AgentTool;
import java.util.Map;

public class AgentToolMapper {

    public static AgentTool toEntity(Agent agent, Tool tool, Map<String, Object> config) {
        return new AgentTool(agent, tool, config);
    }

    public static AgentToolResponseDto toResponse(AgentTool entity) {
        return new AgentToolResponseDto(
                entity.getId(),
                entity.getAgent().getId(),
                entity.getTool().getId(),
                entity.getConfig(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
