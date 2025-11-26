package com.agent_java.orchestrator.integration.agent;

import com.agent_java.orchestrator.entity.agent.Agent;
import com.agent_java.orchestrator.entity.agent.AgentTool;
import com.agent_java.orchestrator.entity.agent.knowledge.AgentKnowledge;
import com.agent_java.orchestrator.entity.agent.mapping.AgentKnowledgeMapping;
import com.agent_java.orchestrator.entity.agent.mapping.AgentToolMapping;
import com.agent_java.orchestrator.integration.BaseIntegrationTest;
import com.agent_java.orchestrator.repository.AgentKnowledgeMappingRepository;
import com.agent_java.orchestrator.repository.AgentKnowledgeRepository;
import com.agent_java.orchestrator.repository.AgentRepository;
import com.agent_java.orchestrator.repository.AgentToolMappingRepository;
import com.agent_java.orchestrator.repository.AgentToolRepository;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
public class AgentAssignmentControllerIT extends BaseIntegrationTest {

    private AgentRepository agentRepository;
    private AgentToolRepository toolRepository;
    private AgentToolMappingRepository mappingRepository;
    private AgentKnowledgeRepository knowledgeRepository;
    private AgentKnowledgeMappingRepository agentKnowledgeMappingRepository;

    private Agent agent;
    private AgentTool activeTool;
    private AgentKnowledge knowledge;

    @Autowired
    public AgentAssignmentControllerIT(AgentRepository agentRepository, AgentToolRepository toolRepository, AgentToolMappingRepository mappingRepository, AgentKnowledgeRepository knowledgeRepository, AgentKnowledgeMappingRepository agentKnowledgeMappingRepository) {
        this.agentRepository = agentRepository;
        this.toolRepository = toolRepository;
        this.mappingRepository = mappingRepository;
        this.knowledgeRepository = knowledgeRepository;
        this.agentKnowledgeMappingRepository = agentKnowledgeMappingRepository;
    }

    @BeforeAll
    void setUp() {
        agent = agentRepository.save(new Agent("Agent Smith", "T-800"));

        activeTool = new AgentTool("Laser Gun");
        activeTool.setActive(true);
        activeTool = toolRepository.save(activeTool);

        knowledge = knowledgeRepository.save(new AgentKnowledge("K1-" + UUID.randomUUID()));
    }

    @Test
    public void POST_assign_tool_should_assign_tool_to_agent() throws Exception {
        mockMvc.perform(
                postAuth("/api/agents/" + agent.getId() + "/tools/" + activeTool.getId(), new Object(), List.of("ROLE_ADMIN"), List.of())
        ).andExpect(status().isOk());

        // Verify mapping exists
        var assignedTools = mappingRepository.findByAgentId(agent.getId());

        assert assignedTools.stream()
                .anyMatch(t -> t.getTool().getId().equals(activeTool.getId()));
    }

    @Test
    public void DELETE_unassign_tool_should_remove_tool_from_agent() throws Exception {
        // Assign first
        mappingRepository.save(AgentToolMapping.of(agent, activeTool));

        mockMvc.perform(
                deleteAuth("/api/agents/" + agent.getId() + "/tools/" + activeTool.getId(), List.of("ROLE_ADMIN"), List.of())
        ).andExpect(status().isOk());

        var assignedTools = mappingRepository.findByAgentId(agent.getId());
        assert assignedTools.stream().noneMatch((t) -> t.getTool().getId().equals(activeTool.getId()));
    }

    @Test
    public void GET_list_tools_should_return_assigned_tools() throws Exception {
        mappingRepository.save(AgentToolMapping.of(agent, activeTool));

        mockMvc.perform(
                getAuth("/api/agents/" + agent.getId() + "/tools", List.of("ROLE_ADMIN"), List.of())
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(activeTool.getId().toString()))
                .andExpect(jsonPath("$[0].name").value(activeTool.getName()));
    }

    @Test
    public void POST_assign_knowledge_should_assign_knowledge_to_agent() throws Exception {
        mockMvc.perform(
                postAuth("/api/agents/" + agent.getId() + "/knowledge/" + knowledge.getId(), new Object(), List.of("ROLE_ADMIN"), List.of())
        ).andExpect(status().isOk());

        var assignedKnowledge = agentKnowledgeMappingRepository.findByAgentId(agent.getId());

        assert assignedKnowledge.stream().anyMatch((t) -> t.getKnowledge().getId().equals(knowledge.getId()));
    }

    @Test
    public void DELETE_unassign_knowledge_should_remove_knowledge_from_agent() throws Exception {
        agentKnowledgeMappingRepository.save(AgentKnowledgeMapping.of(agent, knowledge));

        mockMvc
                .perform(
                        deleteAuth("/api/agents/" + agent.getId() + "/knowledge/" + knowledge.getId(), List.of("ROLE_ADMIN"), List.of())
                ).andExpect(status().isOk());

        var assignedKnowledge = agentKnowledgeMappingRepository.findByAgentId(agent.getId());
        assert assignedKnowledge.stream().noneMatch((t) -> t.getKnowledge().getId().equals(knowledge.getId()));
    }

    @Test
    public void GET_list_knowledge_should_return_assigned_knowledge() throws Exception {
        agentKnowledgeMappingRepository.save(AgentKnowledgeMapping.of(agent, knowledge));

        mockMvc.perform(
                getAuth("/api/agents/" + agent.getId() + "/knowledge", List.of("ROLE_ADMIN"), List.of())
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(knowledge.getId().toString()))
                .andExpect(jsonPath("$[0].name").value(knowledge.getName()));
    }
}
