package com.agent_java.orchestrator.dto;

import java.util.Map;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgentResponseDto {

    UUID id;
    String name;
    String description;
    boolean active;
    String provider;
    String baseUrl;
    String apiKey;
    String chatCompletionsPath;
    String model;
    String embeddingModel;
    int dimension;
    String embeddingsPath;
    double topP;
    double temperature;
    int maxTokens;
    double frequencyPenalty;
    double presencePenalty;
    Map<String, Object> settings;
}
