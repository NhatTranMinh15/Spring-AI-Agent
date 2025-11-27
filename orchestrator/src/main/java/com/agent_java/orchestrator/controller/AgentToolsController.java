package com.agent_java.orchestrator.controller;

import com.agent_java.orchestrator.service.AgentToolAssignmentService;
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

    private final AgentToolAssignmentService toolAssignmentService;

    @Autowired
    public AgentToolsController(AgentToolAssignmentService toolAssignmentService) {
        this.toolAssignmentService = toolAssignmentService;
    }

    @GetMapping
    public ResponseEntity list(@PathVariable UUID agentId) {
        var result = toolAssignmentService.getTools(agentId);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/{toolId}")
    public ResponseEntity assign(
            @PathVariable UUID agentId,
            @PathVariable UUID toolId
    ) {
        toolAssignmentService.assignTool(agentId, toolId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{toolId}")
    public ResponseEntity unassign(
            @PathVariable UUID agentId,
            @PathVariable UUID toolId
    ) {
        toolAssignmentService.unassignTool(agentId, toolId);
        return ResponseEntity.ok().build();
    }
}
