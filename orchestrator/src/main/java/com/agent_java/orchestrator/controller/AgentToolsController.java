package com.agent_java.orchestrator.controller;

import com.agent_java.orchestrator.service.AgentToolService;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/agents/{agentId}/tools")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AgentToolsController {

    private final AgentToolService agentToolService;

    @Autowired
    public AgentToolsController(AgentToolService agentToolService) {
        this.agentToolService = agentToolService;
    }

    @GetMapping
    public ResponseEntity list(@PathVariable UUID agentId) {
        var result = agentToolService.getTools(agentId);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/{toolId}")
    public ResponseEntity assign(
            @PathVariable UUID agentId,
            @PathVariable UUID toolId
    ) {
        agentToolService.assignTool(agentId, toolId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{toolId}")
    public ResponseEntity unassign(
            @PathVariable UUID agentId,
            @PathVariable UUID toolId
    ) {
        agentToolService.unassignTool(agentId, toolId);
        return ResponseEntity.ok().build();
    }
}
