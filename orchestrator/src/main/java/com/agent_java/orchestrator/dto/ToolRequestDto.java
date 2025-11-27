package com.agent_java.orchestrator.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.Map;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ToolRequestDto {

    @NotBlank
    String name;

    String type = null;

    String description = null;

    Map<String, Object> config = null;

    boolean active = true;
}
