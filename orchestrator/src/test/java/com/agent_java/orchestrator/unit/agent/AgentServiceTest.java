package com.agent_java.orchestrator.unit.agent;

import com.agent_java.orchestrator.dto.AgentRequestDto;
import com.agent_java.orchestrator.entity.agent.Agent;
import com.agent_java.orchestrator.mapper.AgentMapper;
import com.agent_java.orchestrator.repository.AgentRepository;
import com.agent_java.orchestrator.service.AgentService;
import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AgentServiceTest {

    @Mock
    private AgentRepository repo;

    @InjectMocks
    private AgentService service;

    @BeforeEach
    private void setup() {
        Mockito.reset(repo);
    }

    // --- getAllActive ---
    @Test
    public void getAllActive_should_return_list_of_active_agents() {
        var agent = buildAgent();
        when(repo.findAllByActiveTrue()).thenReturn(List.of(agent));

        var result = service.getAllActive();

        assertEquals(1, result.size());
        assertEquals(agent.getName(), result.get(0).getName());
        verify(repo, times(1)).findAllByActiveTrue();
    }

    // --- getById ---
    @Test
    public void getById_should_return_agent_response() {
        var id = UUID.randomUUID();
        var agent = buildAgent(id);
        when(repo.findById(id)).thenReturn(Optional.of(agent));

        var result = service.getById(id);

        assertEquals(agent.getId(), result.getId());
        assertEquals(agent.getName(), result.getName());
        verify(repo).findById(id);
    }

    @Test
    public void getById_should_throw_EntityNotFoundException_when_not_found() {
        var id = UUID.randomUUID();
        when(repo.findById(id)).thenReturn(Optional.empty());

        var exception = assertThrows(EntityNotFoundException.class, () -> service.getById(id));

        assertTrue(exception.getMessage().contains("Agent not found"));
        verify(repo).findById(id);
    }

    // --- create ---
    @Test
    public void create_should_save_new_agent_and_return_response() {
        var request = buildRequest();
        var entity = AgentMapper.toEntity(request);
        var saved = new Agent(entity.getName(), entity.getModel(), entity.getDescription(), entity.getTemperature(), entity.getMaxTokens(), entity.getTopP(), entity.getFrequencyPenalty(), entity.getPresencePenalty(), entity.getProvider(), entity.getSettings());
        saved.setId(UUID.randomUUID());

        when(repo.save(any())).thenReturn(saved);

        var result = service.create(request);

        assertEquals(saved.getName(), result.getName());
        assertNotNull(result.getId());
        verify(repo).save(any());
    }

    // --- update ---
    @Test
    public void update_should_modify_existing_agent_and_return_updated_response() {
        var id = UUID.randomUUID();
        var existing = buildAgent(id);
        var request = buildRequest("Updated Agent", 1.0);

        when(repo.findById(id)).thenReturn(Optional.of(existing));
        when(repo.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        var result = service.update(id, request);

        assertEquals("Updated Agent", result.getName());
        assertEquals(1.0, result.getTemperature());
        verify(repo).findById(id);
        verify(repo).save(existing);
    }

    @Test
    public void update_should_throw_EntityNotFoundException_when_agent_missing() {
        var id = UUID.randomUUID();
        when(repo.findById(id)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> service.update(id, buildRequest()));

        assertTrue(exception.getMessage().contains("Agent not found"));
        verify(repo).findById(id);
    }

    // --- softDelete ---
    @Test
    public void softDelete_should_set_deletedAt_timestamp() {
        var id = UUID.randomUUID();
        Agent existing = buildAgent(id);

        when(repo.findById(id)).thenReturn(Optional.of(existing));
        when(repo.save(any())).thenReturn(existing);

        service.softDelete(id);

        assertNotNull(existing.getDeletedAt());
        verify(repo).findById(id);
        verify(repo).save(existing);
    }

    @Test
    public void softDelete_should_throw_EntityNotFoundException_when_not_found() {
        var id = UUID.randomUUID();
        when(repo.findById(id)).thenReturn(Optional.empty());
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> service.softDelete(id));

        assertTrue(exception.getMessage().contains("Agent not found"));
        verify(repo).findById(id);
    }

    // --- helpers ---
    private Agent buildAgent(UUID id) {
        var agent = new Agent(
                "Test Agent",
                "gpt-4o-mini",
                "desc",
                new BigDecimal("0.7"),
                2048,
                BigDecimal.ONE,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                "",
                new HashMap<>());
        agent.setActive(true);
        agent.setId(id);
        agent.setCreatedAt(OffsetDateTime.now());
        return agent;
    }

    private AgentRequestDto buildRequest(String name, double temperature) {
        return new AgentRequestDto(name, "gpt-4o-mini", "desc", temperature, 2048, 1.0, 0.0, 0.0, true, "openai", Map.of("max_retries", 3));
    }

    private Agent buildAgent() {
        return buildAgent(UUID.randomUUID());
    }

    private AgentRequestDto buildRequest() {
        return buildRequest("Test Agent", 0.7);
    }
}
