package com.agent_java.orchestrator.mapper;

import com.agent_java.orchestrator.dto.ToolRequestDto;
import com.agent_java.orchestrator.dto.ToolResponseDto;
import com.agent_java.orchestrator.entity.Tool;

public class ToolMapper {

    public static Tool toEntity(ToolRequestDto req) {
        var tool = new Tool(req.getName(), req.getType(), req.getDescription(), req.getConfig());
        tool.setActive(true);
        return tool;
    }

    public static ToolResponseDto toResponse(Tool entity) {
        return new ToolResponseDto(
                entity.getId(),
                entity.getName(),
                entity.getType(),
                entity.getDescription(),
                entity.getConfig(),
                entity.isActive()
        );
    }
}
