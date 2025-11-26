package com.agent_java.orchestrator.service;

import com.agent_java.orchestrator.dto.AgentToolRequestDto;
import com.agent_java.orchestrator.dto.AgentToolResponseDto;
import com.agent_java.orchestrator.mapper.AgentToolMapper;
import com.agent_java.orchestrator.repository.AgentToolRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AgentToolService {

    @Autowired
    private AgentToolRepository repo;

    @Transactional(readOnly = true)
    public List<AgentToolResponseDto> getAllActive() {
        return repo.findAllByActiveTrue().stream().map(AgentToolMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public AgentToolResponseDto getById(UUID id) {
        var entity = repo.findById(id).orElseThrow(() -> new EntityNotFoundException("Tool not found: " + id));
        return AgentToolMapper.toResponse(entity);
    }

    @Transactional
    public AgentToolResponseDto create(AgentToolRequestDto request) {
        var entity = AgentToolMapper.toEntity(request);
        return AgentToolMapper.toResponse(repo.save(entity));
    }

    @Transactional
    public AgentToolResponseDto update(UUID id, AgentToolRequestDto request) {
        var existing = repo.findById(id).orElseThrow(() -> new EntityNotFoundException("Tool not found: " + id));
        existing.setName(request.getName());
        existing.setType(request.getType());
        existing.setDescription(request.getDescription());
        existing.setConfig(request.getConfig());
        existing.setActive(request.isActive());
        return AgentToolMapper.toResponse(repo.save(existing));
    }

    @Transactional
    public void softDelete(UUID id) {
        var tool = repo.findById(id).orElseThrow(() -> new EntityNotFoundException("Tool not found: " + id));
        tool.markDeleted();
        repo.save(tool);
    }
}
