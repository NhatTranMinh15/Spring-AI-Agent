package com.agent_java.orchestrator.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ToolRequestDto {

    @NotBlank
    String name;

    String type = null;

    String description = null;

    Map<String, Object> config = null;

    boolean active = true;

    public ToolRequestDto(String name, String type, String description, Map<String, Object> config) {
        this.name = name;
        this.type = type;
        this.description = description;
        this.config = config;
    }
}
