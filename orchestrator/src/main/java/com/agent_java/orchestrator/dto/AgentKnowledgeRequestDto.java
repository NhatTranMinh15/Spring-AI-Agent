package com.agent_java.orchestrator.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgentKnowledgeRequestDto {

    @NotBlank
    String name;
    String sourceType = null;
    String sourceUri = null;
    Map<String, Object> metadata = null;
    String embeddingModel = null;
    Boolean active = true;
}
