package com.agent_java.orchestrator.service;

import com.agent_java.orchestrator.dto.AgentKnowledgeRequestDto;
import com.agent_java.orchestrator.dto.AgentKnowledgeResponseDto;
import com.agent_java.orchestrator.mapper.AgentKnowledgeMapper;
import com.agent_java.orchestrator.repository.AgentKnowledgeRepository;
import com.agent_java.orchestrator.repository.AgentRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AgentKnowledgeService {

    private final AgentKnowledgeRepository repo;
    private final AgentRepository agentRepo;

    @Autowired
    public AgentKnowledgeService(AgentKnowledgeRepository repo, AgentRepository agentRepo) {
        this.repo = repo;
        this.agentRepo = agentRepo;
    }

    @Transactional(readOnly = true)
    public List<AgentKnowledgeResponseDto> getByAgent(UUID agentId) {
        return repo.findAllByAgentIdAndActiveTrue(agentId).stream().map(AgentKnowledgeMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public AgentKnowledgeResponseDto getOneForAgent(UUID agentId, UUID knowledgeId) {
        var result = repo.findByIdAndAgentId(knowledgeId, agentId)
                .orElseThrow(() -> new EntityNotFoundException("Knowledge " + knowledgeId + " not found for agent " + agentId));
        return AgentKnowledgeMapper.toResponse(result);
    }

    @Transactional
    public AgentKnowledgeResponseDto create(UUID agentId, AgentKnowledgeRequestDto request) {
        var agent = agentRepo.findById(agentId).orElseThrow(() -> new EntityNotFoundException("Agent not found: " + agentId));
        var entity = AgentKnowledgeMapper.toEntity(agent, request);
        return AgentKnowledgeMapper.toResponse(repo.save(entity));
    }

    @Transactional
    public AgentKnowledgeResponseDto update(UUID agentId, UUID knowledgeId, AgentKnowledgeRequestDto request) {
        var existing = repo.findById(knowledgeId).orElseThrow(() -> new EntityNotFoundException("Knowledge not found: " + knowledgeId));

        // Agent cannot be changed --> validate agentId
        if (existing.getAgent().getId() != agentId) {
            throw new IllegalArgumentException("Agent cannot be changed for this knowledge item");
        }

        existing.setName(request.getName());
        existing.setSourceType(request.getSourceType());
        existing.setSourceUri(request.getSourceUri());
        existing.setMetadata(request.getMetadata());
        existing.setEmbeddingModel(request.getEmbeddingModel());
        existing.setActive(request.getActive());
        return AgentKnowledgeMapper.toResponse(repo.save(existing));
    }

    @Transactional
    public void softDelete(UUID agentId, UUID knowledgeId) {
        var knowledge = repo.findById(knowledgeId).orElseThrow(() -> new EntityNotFoundException("Knowledge " + knowledgeId + " not found for agent " + agentId));

        if (knowledge.getAgent().getId() != agentId) {
            throw new IllegalArgumentException("Knowledge " + knowledgeId + " does not belong to agent " + agentId);
        }

        knowledge.markDeleted();
        repo.save(knowledge);
    }
}
