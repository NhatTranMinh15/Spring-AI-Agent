package com.agent_java.orchestrator.mapper;

import com.agent_java.orchestrator.dto.AgentKnowledgeRequestDto;
import com.agent_java.orchestrator.dto.AgentKnowledgeResponseDto;
import com.agent_java.orchestrator.entity.agent.knowledge.AgentKnowledge;

public class AgentKnowledgeMapper {

    public static AgentKnowledge toEntity(AgentKnowledgeRequestDto req) {
        AgentKnowledge agent = new AgentKnowledge(
                req.getName(),
                req.getSourceType(),
                req.getSourceUri(),
                req.getMetadata(),
                req.getEmbeddingModel()
        );
        agent.setActive(true);
        return agent;
    }

    public static AgentKnowledgeResponseDto toResponse(AgentKnowledge entity) {
        return new AgentKnowledgeResponseDto(
                entity.getId(),
                entity.getName(),
                entity.getSourceType(),
                entity.getSourceUri(),
                entity.getMetadata(),
                entity.getEmbeddingModel(),
                entity.isActive()
        );
    }
}
