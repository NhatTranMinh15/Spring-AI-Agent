package com.agent_java.orchestrator.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgentKnowledgeImportResponseDto {

    AgentKnowledgeResponseDto knowledge;

    int numberOfChunks;

    String fileName;
}
