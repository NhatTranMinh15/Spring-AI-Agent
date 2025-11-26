package com.agent_java.orchestrator.controller;

import com.agent_java.orchestrator.dto.AgentKnowledgeRequestDto;
import com.agent_java.orchestrator.dto.AgentKnowledgeResponseDto;
import com.agent_java.orchestrator.service.AgentKnowledgeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
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
@RequestMapping("/api/knowledge")
@PreAuthorize("hasRole('ROLE_ADMIN')")
@Tag(name = "Agent Knowledge", description = "Manage knowledge sources assignable to agents")
public class AgentKnowledgeController {

    private final AgentKnowledgeService service;

    @Autowired
    public AgentKnowledgeController(AgentKnowledgeService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity getAllActive() {
        List<AgentKnowledgeResponseDto> result = service.getAllActive();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity getById(@PathVariable UUID id) {
        AgentKnowledgeResponseDto result = service.getById(id);
        return ResponseEntity.ok(result);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity create(@Valid @RequestBody AgentKnowledgeRequestDto req) {
        AgentKnowledgeResponseDto result = service.create(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity update(@PathVariable UUID id, @Valid @RequestBody AgentKnowledgeRequestDto req) {
        AgentKnowledgeResponseDto result = service.update(id, req);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity delete(@PathVariable UUID id) {
        service.softDelete(id);
        return ResponseEntity.noContent().build();
    }

}
