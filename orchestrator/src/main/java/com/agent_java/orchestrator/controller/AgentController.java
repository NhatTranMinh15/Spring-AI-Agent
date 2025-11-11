package com.agent_java.orchestrator.controller;

import com.agent_java.orchestrator.dto.AgentRequestDto;
import com.agent_java.orchestrator.dto.AgentResponseDto;
import com.agent_java.orchestrator.service.AgentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
@RequestMapping("/api/agents")
@Tag(name = "Agent Management", description = "Manage AI Agents for orchestration")
public class AgentController {

    @Autowired
    AgentService service;

    @GetMapping
    @PreAuthorize("hasAuthority('SCOPE_agents.read') or hasRole('ADMIN')")
    @Operation(summary = "List all active agents")
    public List<AgentResponseDto> getAllActive() {
        return service.getAllActive();
    }

    public AgentResponseDto getById(@PathVariable UUID id) {
        return service.getById(id);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('SCOPE_agents.write') or hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new agent")
    public AgentResponseDto create(@Valid @RequestBody AgentRequestDto request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_agents.write') or hasRole('ADMIN')")
    @Operation(summary = "Update an existing agent")
    public AgentResponseDto update(@PathVariable UUID id, @Valid @RequestBody AgentRequestDto request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_agents.write') or hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Soft delete an agent")
    public void delete(@PathVariable UUID id) {
        service.softDelete(id);
    }
}
