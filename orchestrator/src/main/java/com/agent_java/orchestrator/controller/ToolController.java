package com.agent_java.orchestrator.controller;

import com.agent_java.orchestrator.dto.ToolRequestDto;
import com.agent_java.orchestrator.dto.ToolResponseDto;
import com.agent_java.orchestrator.service.ToolService;
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
@Tag(name = "Tools", description = "Manage AI Tools assignable to agents")
public class ToolController {

    private final ToolService service;

    @Autowired
    public ToolController(ToolService service) {
        this.service = service;
    }

    @GetMapping
    public List<ToolResponseDto> getAllActive() {
        return service.getAllActive();
    }

    @GetMapping("/{id}")
    public ToolResponseDto getById(@PathVariable UUID id) {
        return service.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ToolResponseDto create(@Valid @RequestBody ToolRequestDto req) {
        return service.create(req);
    }

    @PutMapping("/{id}")
    public ToolResponseDto update(@PathVariable UUID id, @Valid @RequestBody ToolRequestDto req) {
        return service.update(id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        service.softDelete(id);
    }
}
