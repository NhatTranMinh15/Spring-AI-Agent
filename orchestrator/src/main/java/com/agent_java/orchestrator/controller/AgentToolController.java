package com.agent_java.orchestrator.controller;

import com.agent_java.orchestrator.dto.AgentToolRequestDto;
import com.agent_java.orchestrator.dto.AgentToolResponseDto;
import com.agent_java.orchestrator.service.AgentToolService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
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
@RequestMapping("/api/tools")
@PreAuthorize("hasRole('ROLE_ADMIN')")
@Tag(name = "Agent Tools", description = "Manage AI Tools assignable to agents")
public class AgentToolController {

    private final AgentToolService service;

    @Autowired
    public AgentToolController(AgentToolService service) {
        this.service = service;
    }

    @GetMapping
    public List<AgentToolResponseDto> getAllActive() {
        return service.getAllActive();
    }

    @GetMapping("/{id}")
    public AgentToolResponseDto getById(@PathVariable UUID id) {
        return service.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AgentToolResponseDto create(@Valid @RequestBody AgentToolRequestDto req) {
        return service.create(req);
    }

    @PutMapping("/{id}")
    public AgentToolResponseDto update(@PathVariable UUID id, @Valid @RequestBody AgentToolRequestDto req) {
        return service.update(id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        service.softDelete(id);
    }
}
