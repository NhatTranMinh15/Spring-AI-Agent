package com.agent_java.orchestrator.dto;

import java.time.Instant;
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
    UUID agentId;
    String name;
    String sourceType;
    String sourceUri;
    Map<String, Object> metadata;
    boolean active;
    Instant createdAt;
    Instant updatedAt;

}
