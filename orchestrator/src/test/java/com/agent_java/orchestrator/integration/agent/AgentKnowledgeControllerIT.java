package com.agent_java.orchestrator.integration.agent;

import com.agent_java.orchestrator.dto.AgentKnowledgeRequestDto;
import com.agent_java.orchestrator.entity.agent.knowledge.AgentKnowledge;
import com.agent_java.orchestrator.integration.BaseIntegrationTest;
import com.agent_java.orchestrator.repository.AgentKnowledgeRepository;
import static com.agent_java.orchestrator.support.SoftDeleteAssertions.assertSoftDeleted;
import jakarta.persistence.EntityManager;
import java.util.Map;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class AgentKnowledgeControllerIT extends BaseIntegrationTest {

    private AgentKnowledgeRepository repository;
    private EntityManager entityManager;

    @Autowired
    public AgentKnowledgeControllerIT(AgentKnowledgeRepository repository, EntityManager entityManager) {
        this.repository = repository;
        this.entityManager = entityManager;
    }

    @BeforeEach
    void setup() {
        repository.deleteAll();
    }

    @Test
    public void should_create_a_new_agent_knowledge() throws Exception {
        var req = new AgentKnowledgeRequestDto(
                "Ophthalmology Dataset",
                "URL",
                "https://example.com/ophthalmology",
                Map.of("category", "medical", "language", "en"),
                "text-embedding-3-small",
                true
        );

        mockMvc.perform(
                postAuth("/api/knowledge", req, List.of("ROLE_ADMIN"), List.of())
        )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Ophthalmology Dataset"))
                .andExpect(jsonPath("$.active").value(true))
                .andExpect(jsonPath("$.embeddingModel").value("text-embedding-3-small"));
    }

    @Test
    public void should_get_all_active_knowledge_sources() throws Exception {
        var agentKnowledgeA = new AgentKnowledge("Knowledge A");
        agentKnowledgeA.setSourceType("URL");
        agentKnowledgeA.setSourceUri("https://a.com");
        agentKnowledgeA.setActive(true);
        repository.save(agentKnowledgeA);

        var agentKnowledgeB = new AgentKnowledge("Knowledge B");
        agentKnowledgeB.setSourceType("URL");
        agentKnowledgeB.setSourceUri("https://b.com");
        agentKnowledgeB.setActive(false);
        repository.save(agentKnowledgeB);

        mockMvc.perform(
                getAuth("/api/knowledge", List.of("ROLE_ADMIN"), List.of())
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Knowledge A"))
                .andExpect(jsonPath("$[0].active").value(true));
    }

    @Test
    public void should_get_knowledge_by_id() throws Exception {
        var agentKnowledge = new AgentKnowledge("Knowledge X");
        agentKnowledge.setSourceType("URL");
        agentKnowledge.setSourceUri("https://example.com/x");
        agentKnowledge.setEmbeddingModel("text-embedding-3-large");
        agentKnowledge.setActive(true);
        var entity = repository.save(agentKnowledge);

        mockMvc.perform(
                getAuth("/api/knowledge/" + entity.getId(), List.of("ROLE_ADMIN"), List.of())
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(entity.getId().toString()))
                .andExpect(jsonPath("$.name").value("Knowledge X"))
                .andExpect(jsonPath("$.embeddingModel").value("text-embedding-3-large"));
    }

    @Test
    public void should_update_existing_knowledge() throws Exception {
        var agentKnowledge = new AgentKnowledge(
                "Old Knowledge",
                "URL",
                "https://old.com",
                Map.of(),
                "old-embed"
        );
        agentKnowledge.setActive(true);

        var entity = repository.save(agentKnowledge);

        var updateReq = new AgentKnowledgeRequestDto(
                "Updated Knowledge",
                "URL",
                "https://new.com",
                Map.of("updated", true),
                "text-embedding-3-small",
                true
        );

        mockMvc.perform(
                putAuth("/api/knowledge/" + entity.getId(), updateReq, List.of("ROLE_ADMIN"), List.of())
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Knowledge"))
                .andExpect(jsonPath("$.sourceUri").value("https://new.com"))
                .andExpect(jsonPath("$.embeddingModel").value("text-embedding-3-small"));
    }

    @Test
    public void should_soft_delete_knowledge_and_exclude_from_queries() throws Exception {
        AgentKnowledge agentKnowledge = new AgentKnowledge(
                "Temp Knowledge",
                "URL",
                "https://delete.me",
                Map.of(),
                ""
        );
        agentKnowledge.setActive(true);

        var entity = repository.save(agentKnowledge);

        // Call DELETE API
        mockMvc.perform(
                deleteAuth("/api/knowledge/" + entity.getId(), List.of("ROLE_ADMIN"), List.of())
        ).andExpect(status().isNoContent());

        // Verify soft delete in DB (deleted_at not null)
        assertSoftDeleted(entityManager, AgentKnowledge.class, entity.getId());

        // Verify excluded from GET /api/knowledge
        mockMvc.perform(
                getAuth("/api/knowledge", List.of("ROLE_ADMIN"), List.of())
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    public void should_validate_missing_name() throws Exception {
        var invalidReq = new AgentKnowledgeRequestDto(
                "   ", // blank
                "URL",
                "https://example.com",
                Map.of(),
                "",
                true
        );

        mockMvc.perform(
                postAuth("/api/knowledge", invalidReq, List.of("ROLE_ADMIN"), List.of())
        ).andExpect(status().isBadRequest());
    }
}
