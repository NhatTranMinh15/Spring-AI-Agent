package com.agent_java.orchestrator.service;

import com.agent_java.orchestrator.dto.AgentRequestDto;
import com.agent_java.orchestrator.dto.AgentResponseDto;
import com.agent_java.orchestrator.entity.Agent;
import com.agent_java.orchestrator.mapper.AgentMapper;
import com.agent_java.orchestrator.repository.AgentRepository;
import jakarta.persistence.EntityNotFoundException;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AgentService {

    @Autowired
    AgentRepository repository;
    AgentMapper mapper;

    public List<AgentResponseDto> getAllActive() {
        var actives = repository.findAllActive();
        return actives.stream().map(mapper::toResponse).toList();
    }

    public AgentResponseDto getById(UUID id) {
        var agent = repository.findByIdNotDeleted(id).orElseThrow(() -> new EntityNotFoundException("Agent not found: " + id));
        return mapper.toResponse(agent);
    }

    @Transactional
    public AgentResponseDto create(AgentRequestDto request) {
        var entity = mapper.toEntity(request);
        entity = repository.save(entity);
        return mapper.toResponse(entity);
    }

    @Transactional
    public AgentResponseDto update(UUID id, AgentRequestDto request) {
        Agent existing = repository.findByIdNotDeleted(id).orElseThrow(() -> new EntityNotFoundException("Agent not found: " + id));
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
        return mapper.toResponse(existing);
    }

    @Transactional
    public void softDelete(UUID id) {
        Agent agent = repository.findByIdNotDeleted(id).orElseThrow(() -> new EntityNotFoundException("Agent not found: " + id));
        agent.setDeletedAt(ZonedDateTime.now());
        agent = repository.save(agent);
    }
}
