package com.agent_java.orchestrator.mapper;

import com.agent_java.orchestrator.dto.AgentToolRequestDto;
import com.agent_java.orchestrator.dto.AgentToolResponseDto;
import com.agent_java.orchestrator.entity.agent.AgentTool;

public class AgentToolMapper {

    public static AgentTool toEntity(AgentToolRequestDto req) {
        var agentTool = new AgentTool(
                req.getName(),
                req.getType(),
                req.getDescription(),
                req.getConfig()
        );
        agentTool.setActive(req.isActive());
        return agentTool;
    }

    public static AgentToolResponseDto toResponse(AgentTool entity) {
        return new AgentToolResponseDto(
                entity.getId(),
                entity.getName(),
                entity.getType(),
                entity.getDescription(),
                entity.getConfig(),
                entity.isActive()
        );
    }
}
