package com.agent_java.orchestrator.dto;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgentListResponseDto {

    UUID id;
    String name;
    String model;
    String lastModifiedBy;
    String lastModifiedWhen;
    String lastPublishedWhen;
    String owner;
    String status;
}
