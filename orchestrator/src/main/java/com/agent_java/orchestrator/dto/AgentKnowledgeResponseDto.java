package com.agent_java.orchestrator.dto;

import java.util.Map;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgentKnowledgeResponseDto {

    UUID id;
    String name;
    String sourceType;
    String sourceUri;
    Map<String, Object> metadata;
    String embeddingModel;
    boolean active;
}
