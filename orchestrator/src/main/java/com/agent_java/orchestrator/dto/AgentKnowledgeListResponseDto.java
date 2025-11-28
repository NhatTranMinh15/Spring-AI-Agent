package com.agent_java.orchestrator.dto;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgentKnowledgeListResponseDto {

    UUID id;
    String name;
    String type;
    String availableTo;
    String usage;
    String lastModifiedBy;
    String lastModifiedWhen;
    String status;
}
