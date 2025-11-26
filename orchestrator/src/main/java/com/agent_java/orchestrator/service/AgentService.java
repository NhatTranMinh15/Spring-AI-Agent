package com.agent_java.orchestrator.service;

import com.agent_java.orchestrator.dto.AgentRequestDto;
import com.agent_java.orchestrator.dto.AgentResponseDto;
import com.agent_java.orchestrator.entity.agent.Agent;
import com.agent_java.orchestrator.mapper.AgentMapper;
import com.agent_java.orchestrator.repository.AgentRepository;
import jakarta.persistence.EntityNotFoundException;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AgentService {

    AgentRepository repository;

    @Autowired
    public AgentService(AgentRepository repository) {
        this.repository = repository;
    }

    public List<AgentResponseDto> getAllActive() {
        var actives = repository.findAllByActiveTrue();
        return actives.stream().map(AgentMapper::toResponse).toList();
    }

    public AgentResponseDto getById(UUID id) {
        var agent = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Agent not found: " + id));
        return AgentMapper.toResponse(agent);
    }

    @Transactional
    public AgentResponseDto create(AgentRequestDto request) {
        var entity = AgentMapper.toEntity(request);
        entity = repository.save(entity);
        return AgentMapper.toResponse(entity);
    }

    @Transactional
    public AgentResponseDto update(UUID id, AgentRequestDto request) {
        Agent existing = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Agent not found: " + id));
        existing.setName(request.getName());
        existing.setModel(request.getModel());
        existing.setDescription(request.getDescription());
        existing.setTemperature(request.getTemperature());
        existing.setMaxTokens(request.getMaxTokens());
        existing.setTopP(request.getTopP());
        existing.setFrequencyPenalty(request.getFrequencyPenalty());
        existing.setPresencePenalty(request.getPresencePenalty());
        existing.setActive(request.isActive());
        existing.setProvider(request.getProvider());
        existing.setSettings(request.getSettings());
        existing = repository.save(existing);
        return AgentMapper.toResponse(existing);
    }

    @Transactional
    public void softDelete(UUID id) {
        Agent agent = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Agent not found: " + id));
        agent.setDeletedAt(OffsetDateTime.now());
        repository.save(agent);
    }
}
