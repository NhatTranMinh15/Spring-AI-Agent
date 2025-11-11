package com.agent_java.orchestrator.viewmodel;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class KnowledgeImportingResponseVm {

    String originalFilename;
    int numberOfSegment;
}
