package com.agent_java.orchestrator.service;

import com.agent_java.orchestrator.dto.AgentKnowledgeRequestDto;
import com.agent_java.orchestrator.dto.AgentKnowledgeResponseDto;
import com.agent_java.orchestrator.mapper.AgentKnowledgeMapper;
import com.agent_java.orchestrator.repository.AgentKnowledgeRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AgentKnowledgeService {

    private AgentKnowledgeRepository repo;

    @Autowired
    public AgentKnowledgeService(AgentKnowledgeRepository repo) {
        this.repo = repo;
    }

    @Transactional(readOnly = true)
    public List<AgentKnowledgeResponseDto> getAllActive() {
        return repo.findAllByActiveTrue().stream().map(AgentKnowledgeMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public AgentKnowledgeResponseDto getById(UUID id) {
        var result = repo.findById(id).orElseThrow(() -> new EntityNotFoundException("Knowledge not found: " + id));
        return AgentKnowledgeMapper.toResponse(result);
    }

    @Transactional
    public AgentKnowledgeResponseDto create(AgentKnowledgeRequestDto request) {
        var entity = AgentKnowledgeMapper.toEntity(request);
        return AgentKnowledgeMapper.toResponse(repo.save(entity));
    }

    @Transactional
    public AgentKnowledgeResponseDto update(UUID id, AgentKnowledgeRequestDto request) {
        var existing = repo.findById(id).orElseThrow(() -> new EntityNotFoundException("Knowledge not found: " + id));
        existing.setName(request.getName());
        existing.setSourceType(request.getSourceType());
        existing.setSourceUri(request.getSourceUri());
        existing.setMetadata(request.getMetadata());
        existing.setEmbeddingModel(request.getEmbeddingModel());
        existing.setActive(request.getActive());
        return AgentKnowledgeMapper.toResponse(repo.save(existing));
    }

    @Transactional
    public void softDelete(UUID id) {
        var knowledge = repo.findById(id).orElseThrow(() -> new EntityNotFoundException("Knowledge not found: " + id));
        knowledge.markDeleted();
        repo.save(knowledge);
    }
}
