package com.agent_java.orchestrator.controller;

import com.agent_java.orchestrator.dto.AgentKnowledgeImportResponseDto;
import com.agent_java.orchestrator.dto.AgentKnowledgeRequestDto;
import com.agent_java.orchestrator.service.AgentKnowledgeService;
import com.agent_java.orchestrator.service.KnowledgeImportService;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/agents/{agentId}/knowledge/import")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AgentKnowledgeImportController {

    private final AgentKnowledgeService agentKnowledgeService;
    private final KnowledgeImportService knowledgeImportService;

    @Autowired
    public AgentKnowledgeImportController(AgentKnowledgeService agentKnowledgeService, KnowledgeImportService knowledgeImportService) {
        this.agentKnowledgeService = agentKnowledgeService;
        this.knowledgeImportService = knowledgeImportService;
    }

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public AgentKnowledgeImportResponseDto uploadDocument(
            @PathVariable UUID agentId,
            @RequestPart("file") MultipartFile file,
            @RequestPart("json") AgentKnowledgeRequestDto request
    ) {
        var knowledge = agentKnowledgeService.create(agentId, request);

        var importResult = knowledgeImportService.importDocument(
                agentId,
                knowledge.getId(),
                file
        );

        return new AgentKnowledgeImportResponseDto(
                knowledge,
                importResult.getNumberOfSegment(),
                importResult.getOriginalFilename()
        );
    }
}
