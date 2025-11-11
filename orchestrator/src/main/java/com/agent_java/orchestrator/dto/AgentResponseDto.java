package com.agent_java.orchestrator.dto;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AgentResponseDto {

    UUID id;
    String name;
    String model;
    String description;
    double temperature;
    int maxTokens;
    double topP;
    double frequencyPenalty;
    double presencePenalty;
    boolean active;
    String provider;
    Map<String, Object> settings;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
