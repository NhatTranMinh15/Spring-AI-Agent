package com.agent_java.orchestrator.mapper;

import com.agent_java.orchestrator.dto.AgentToolResponseDto;
import com.agent_java.orchestrator.entity.agent.AgentTool;

public class AgentToolMapper {

    public static AgentToolResponseDto toResponse(AgentTool entity) {
        var updatedBy = entity.getUpdatedBy();
        return new AgentToolResponseDto(
                entity.getTool().getId(),
                entity.getTool().getName(),
                entity.getTool().getType(),
                entity.getAgent().getName(),
                updatedBy != null ? updatedBy.getName() : "Unknown",
                entity.getUpdatedAt().toString(),
                true
        );
    }
}
