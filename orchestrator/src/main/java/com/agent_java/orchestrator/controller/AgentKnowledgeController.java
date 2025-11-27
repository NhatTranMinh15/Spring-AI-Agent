package com.agent_java.orchestrator.controller;

import com.agent_java.orchestrator.dto.AgentKnowledgeRequestDto;
import com.agent_java.orchestrator.dto.AgentKnowledgeResponseDto;
import com.agent_java.orchestrator.service.AgentKnowledgeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/agents/{agentId}/knowledge")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AgentKnowledgeController {

    private final AgentKnowledgeService agentKnowledgeService;

    @Autowired
    public AgentKnowledgeController(AgentKnowledgeService agentKnowledgeService) {
        this.agentKnowledgeService = agentKnowledgeService;
    }

    @GetMapping
    public ResponseEntity list(@PathVariable UUID agentId) {
        List<AgentKnowledgeResponseDto> result = agentKnowledgeService.getByAgent(agentId);
        return ResponseEntity.ok(result);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity create(
            @PathVariable UUID agentId,
            @Valid @RequestBody AgentKnowledgeRequestDto request
    ) {
        AgentKnowledgeResponseDto result = agentKnowledgeService.create(agentId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PutMapping("/{knowledgeId}")
    public ResponseEntity update(
            @PathVariable UUID agentId,
            @PathVariable UUID knowledgeId,
            @Valid @RequestBody AgentKnowledgeRequestDto request
    ) {
        AgentKnowledgeResponseDto result = agentKnowledgeService.update(agentId, knowledgeId, request);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{knowledgeId}")
    public ResponseEntity get(
            @PathVariable UUID agentId,
            @PathVariable UUID knowledgeId
    ) {
        AgentKnowledgeResponseDto result = agentKnowledgeService.getOneForAgent(agentId, knowledgeId);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{knowledgeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity delete(
            @PathVariable UUID agentId,
            @PathVariable UUID knowledgeId
    ) {
        agentKnowledgeService.softDelete(agentId, knowledgeId);
        return ResponseEntity.noContent().build();
    }

}
