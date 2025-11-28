package com.agent_java.orchestrator.mapper;

import com.agent_java.orchestrator.dto.AgentListResponseDto;
import com.agent_java.orchestrator.dto.AgentRequestDto;
import com.agent_java.orchestrator.dto.AgentResponseDto;
import com.agent_java.orchestrator.entity.agent.Agent;
import java.math.BigDecimal;

public class AgentMapper {

    public static Agent toEntity(AgentRequestDto request) {
        var agent = new Agent(
                request.getName(),
                request.getModel(),
                request.getDescription(),
                toBigDecimalOrDefault(request.getTemperature(), Agent.DEFAULT_TEMPERATURE),
                request.getMaxTokens(),
                toBigDecimalOrDefault(request.getTopP(), Agent.DEFAULT_TOP_P),
                toBigDecimalOrDefault(request.getFrequencyPenalty(), Agent.DEFAULT_FREQUENCY_PENALTY),
                toBigDecimalOrDefault(request.getPresencePenalty(), Agent.DEFAULT_PRESENCE_PENALTY),
                request.getProvider(),
                request.getSettings(),
                request.getBaseUrl(),
                request.getApiKey(),
                request.getChatCompletionsPath(),
                request.getEmbeddingsPath(),
                request.getEmbeddingModel(),
                request.getDimension()
        );
        agent.setActive(request.isActive());
        return agent;
    }

    public static AgentResponseDto toResponse(Agent agent) {
        return new AgentResponseDto(
                agent.getId(),
                agent.getName(),
                agent.getDescription(),
                agent.isActive(),
                agent.getProvider(),
                agent.getBaseUrl(),
                agent.getApiKey(),
                agent.getChatCompletionsPath(),
                agent.getModel(),
                agent.getEmbeddingModel(),
                agent.getDimension(),
                agent.getEmbeddingsPath(),
                agent.getTopP().doubleValue(),
                agent.getTemperature().doubleValue(),
                agent.getMaxTokens(),
                agent.getFrequencyPenalty().doubleValue(),
                agent.getPresencePenalty().doubleValue(),
                agent.getSettings()
        );
    }

    public static AgentListResponseDto toListResponse(Agent agent) {
        var createdBy = agent.getCreatedBy();
        var updatedBy = agent.getUpdatedBy();
        return new AgentListResponseDto(
                agent.getId(),
                agent.getName(),
                agent.getModel(),
                updatedBy != null ? updatedBy.getName() : "Unknown",
                agent.getUpdatedAt().toString(),
                "",
                createdBy != null ? createdBy.getName() : "Unknown",
                agent.isActive() ? "Active" : "Inactive"
        );
    }

    /**
     * Converts a nullable Double to BigDecimal safely,
     * or falls back to a provided default BigDecimal.
     */
    private static BigDecimal toBigDecimalOrDefault(Double d, BigDecimal def) {
        return d != null ? new BigDecimal(d) : def;
    }
}
