package com.agent_java.orchestrator.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.Map;
import lombok.Data;

@Data
public class KnowledgeChunkRequestDto {

    @NotBlank
    String content;
    Map<String, Object> metadata = null;
}
