package com.agent_java.orchestrator.integration.agent;

import com.agent_java.orchestrator.dto.AgentRequestDto;
import com.agent_java.orchestrator.integration.BaseIntegrationTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("AgentController Integration Tests")
public class AgentControllerIT extends BaseIntegrationTest {

    private final ObjectMapper mapper;

    @Autowired
    public AgentControllerIT(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Test
    @DisplayName("should create and retrieve agent successfully")
    public void shouldCreateAndFetchAgent() throws Exception {
        var request
                = new AgentRequestDto(
                        "API Agent",
                        "gpt-4o-mini",
                        "Integration test agent",
                        0.7,
                        2048,
                        1.0,
                        0.0,
                        0.0,
                        true,
                        "",
                        Map.of()
                );
        var createResult = mockMvc.perform(
                postAuth("/api/agents", request, List.of("SCOPE_chatbot.write"), List.of())
        )
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        var createdJson = createResult.getResponse().getContentAsString();
        var createdId = mapper.readTree(createdJson).get("id").asText();

        var fetchResult = mockMvc.perform(
                getAuth("/api/agents/" + createdId, List.of(), List.of("SCOPE_chatbot.read"))
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        var fetchedJson = fetchResult.getResponse().getContentAsString();
        var fetchedName = mapper.readTree(fetchedJson).get("name").asText();
        assertThat(fetchedName).isEqualTo("API Agent");
    }

//    @Test
//    @DisplayName("should return 404 for missing agent")
//    public void shouldReturn404ForMissingAgent() throws Exception {
//        var randomId = UUID.randomUUID();
//
//        mockMvc.perform(
//                getAuth("/api/agents/" + randomId, List.of(), List.of("SCOPE_chatbot.read"))
//        )
//                .andExpect(status().isNotFound())
//                .andExpect(jsonPath("$.error").value("Not Found"));
//    }
}
