package com.agent_java.orchestrator.controller;

import com.agent_java.orchestrator.mapper.AgentKnowledgeMapper;
import com.agent_java.orchestrator.mapper.AgentToolMapper;
import com.agent_java.orchestrator.service.AgentKnowledgeAssignmentService;
import com.agent_java.orchestrator.service.AgentToolAssignmentService;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@RequestMapping("/api/agents/{agentId}")
@PreAuthorize("hasRole('ROLE_ADMIN')")
@Tag(name = "Agent Assignments", description = "Assign tools and knowledge to agents")
public class AgentAssignmentController {

    private final AgentToolAssignmentService toolAssignmentService;
    private final AgentKnowledgeAssignmentService knowledgeAssignmentService;

    @Autowired
    public AgentAssignmentController(AgentToolAssignmentService toolAssignmentService, AgentKnowledgeAssignmentService knowledgeAssignmentService) {
        this.toolAssignmentService = toolAssignmentService;
        this.knowledgeAssignmentService = knowledgeAssignmentService;
    }

    @PostMapping("/tools/{toolId}")
    public ResponseEntity assignTool(@PathVariable UUID agentId, @PathVariable UUID toolId) {
        toolAssignmentService.assignTool(agentId, toolId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/tools/{toolId}")
    public ResponseEntity unassignTool(@PathVariable UUID agentId, @PathVariable UUID toolId) {
        toolAssignmentService.unassignTool(agentId, toolId);
        return ResponseEntity.ok().build();

    }

    @GetMapping("/tools")
    public ResponseEntity listTools(@PathVariable UUID agentId) {
        var result = toolAssignmentService.getTools(agentId).stream().map(AgentToolMapper::toResponse);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/knowledge/{knowledgeId}")
    public ResponseEntity assignKnowledge(@PathVariable UUID agentId, @PathVariable UUID knowledgeId) {
        knowledgeAssignmentService.assignKnowledge(agentId, knowledgeId);
        return ResponseEntity.ok().build();

    }

    @DeleteMapping("/knowledge/{knowledgeId}")
    public ResponseEntity unassignKnowledge(@PathVariable UUID agentId, @PathVariable UUID knowledgeId) {
        knowledgeAssignmentService.unassignKnowledge(agentId, knowledgeId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/knowledge")
    public ResponseEntity listKnowledge(@PathVariable UUID agentId) {
        var result = knowledgeAssignmentService.getKnowledge(agentId).stream().map(AgentKnowledgeMapper::toResponse);
        return ResponseEntity.ok(result);
    }

}
