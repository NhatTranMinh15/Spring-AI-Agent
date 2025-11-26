package com.agent_java.orchestrator.integration.agent;

import com.agent_java.orchestrator.dto.AgentKnowledgeRequestDto;
import com.agent_java.orchestrator.entity.agent.knowledge.AgentKnowledge;
import com.agent_java.orchestrator.integration.BaseIntegrationTest;
import com.agent_java.orchestrator.repository.AgentKnowledgeRepository;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AgentKnowledgeServiceIT extends BaseIntegrationTest {

    private AgentKnowledgeRepository repository;
    private EntityManager entityManager;

    @Autowired
    public AgentKnowledgeServiceIT(AgentKnowledgeRepository repository, EntityManager entityManager) {
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
        AgentKnowledge agentKnowledgeA = new AgentKnowledge(
                "Knowledge A",
                "URL",
                "https://a.com",
                Map.of(),
                ""
        );
        agentKnowledgeA.setActive(true);
        repository.save(agentKnowledgeA);

        AgentKnowledge agentKnowledgeB = new AgentKnowledge(
                "Knowledge B",
                "URL",
                "https://b.com",
                Map.of(),
                ""
        );
        agentKnowledgeB.setActive(false);
        repository.save(agentKnowledgeB);

        mockMvc.perform(
                getAuth("/api/knowledge", List.of("ROLE_ADMIN"), List.of())
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Knowledge A"))
                .andExpect(jsonPath("$[0].active").value(true));
    }
}
