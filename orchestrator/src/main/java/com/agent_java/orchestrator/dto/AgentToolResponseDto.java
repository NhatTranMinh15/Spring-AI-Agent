package com.agent_java.orchestrator.dto;

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
    String name;
    String type;
    String description;
    Map<String, Object> config;
    boolean active;
}
