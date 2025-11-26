package com.agent_java.orchestrator.mapper;

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
                request.getSettings()
        );
        agent.setActive(request.isActive());
        return agent;
    }

    public static AgentResponseDto toResponse(Agent agent) {
        return new AgentResponseDto(
                agent.getId(),
                agent.getName(),
                agent.getModel(),
                agent.getDescription(),
                agent.getTemperature().doubleValue(),
                agent.getMaxTokens(),
                agent.getTopP().doubleValue(),
                agent.getFrequencyPenalty().doubleValue(),
                agent.getPresencePenalty().doubleValue(),
                agent.isActive(),
                agent.getProvider(),
                agent.getSettings()
        );
    }

    private static BigDecimal toBigDecimalOrDefault(Double d, BigDecimal def) {
        return d != null ? new BigDecimal(d) : def;
    }
}
