package com.agent_java.orchestrator.dto;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgentToolResponseDto {

    UUID id;
    UUID agentId;
    UUID toolId;
    Map<String, Object> config = null;
    OffsetDateTime createdAt;
    OffsetDateTime updatedAt;
}
