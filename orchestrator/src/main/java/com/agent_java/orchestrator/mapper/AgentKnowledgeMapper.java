package com.agent_java.orchestrator.mapper;

import com.agent_java.orchestrator.dto.AgentKnowledgeRequestDto;
import com.agent_java.orchestrator.dto.AgentKnowledgeResponseDto;
import com.agent_java.orchestrator.entity.agent.Agent;
import com.agent_java.orchestrator.entity.agent.knowledge.AgentKnowledge;
import java.time.OffsetDateTime;

public class AgentKnowledgeMapper {

    public static AgentKnowledge toEntity(Agent agent, AgentKnowledgeRequestDto request) {
        AgentKnowledge agentKnowledge = new AgentKnowledge(
                agent,
                request.getName(),
                request.getSourceType(),
                request.getSourceUri(),
                request.getMetadata(),
                request.getEmbeddingModel()
        );
        return agentKnowledge;
    }

    public static AgentKnowledgeResponseDto toResponse(AgentKnowledge entity) {
        return new AgentKnowledgeResponseDto(
                entity.getId(),
                entity.getAgent().getId(),
                entity.getName(),
                entity.getSourceType(),
                entity.getSourceUri(),
                entity.getMetadata(),
                entity.getEmbeddingModel(),
                entity.isActive(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
