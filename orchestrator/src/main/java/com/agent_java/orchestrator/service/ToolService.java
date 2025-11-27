package com.agent_java.orchestrator.service;

import com.agent_java.orchestrator.dto.ToolRequestDto;
import com.agent_java.orchestrator.dto.ToolResponseDto;
import com.agent_java.orchestrator.mapper.ToolMapper;
import com.agent_java.orchestrator.repository.ToolRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ToolService {

    private final ToolRepository repo;

    @Autowired
    public ToolService(ToolRepository repo) {
        this.repo = repo;
    }

    @Transactional(readOnly = true)
    public List<ToolResponseDto> getAllActive() {
        return repo.findAllByActiveTrue().stream().map(ToolMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public ToolResponseDto getById(UUID id) {
        var entity = repo.findById(id).orElseThrow(() -> new EntityNotFoundException("Tool not found:" + id));
        return ToolMapper.toResponse(entity);
    }

    @Transactional
    public ToolResponseDto create(ToolRequestDto request) {
        var entity = ToolMapper.toEntity(request);
        return ToolMapper.toResponse(repo.save(entity));
    }

    @Transactional
    public ToolResponseDto update(UUID id, ToolRequestDto request) {
        var existing = repo.findById(id).orElseThrow(() -> new EntityNotFoundException("Tool not found: " + id));
        existing.setName(request.getName());
        existing.setType(request.getType());
        existing.setDescription(request.getDescription());
        existing.setConfig(request.getConfig());
        existing.setActive(request.isActive());

        return ToolMapper.toResponse(repo.save(existing));
    }

    @Transactional
    public void softDelete(UUID id) {
        var tool = repo.findById(id).orElseThrow(() -> new EntityNotFoundException("Tool not found: " + id));

        tool.markDeleted();
        repo.save(tool);
    }
}
