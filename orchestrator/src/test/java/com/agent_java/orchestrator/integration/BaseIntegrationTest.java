package com.agent_java.orchestrator.integration;

import com.agent_java.orchestrator.integration.config.PostgresTestContainer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@SpringBootTest
@ImportAutoConfiguration(
        exclude = {
            org.springframework.ai.mcp.client.autoconfigure.McpClientAutoConfiguration.class,
            org.springframework.ai.mcp.client.autoconfigure.McpToolCallbackAutoConfiguration.class,
            org.springframework.ai.model.tool.autoconfigure.ToolCallingAutoConfiguration.class
        }
)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // Container spins up once per class
@ContextConfiguration(initializers = {PostgresTestContainer.class})
@Tag("integration")
@ActiveProfiles("test")
public abstract class BaseIntegrationTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    protected String asJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(BaseIntegrationTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    /**
     * Helper: build JWT with custom roles and scopes.
     * Roles must NOT include "ROLE_" prefix; scopes must NOT include "SCOPE_" prefix.
     *
     * @param roles
     * @param scopes
     * @return
     */
    protected SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor jwtWith(List<String> roles, List<String> scopes) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String role : roles) {
            authorities.add(new SimpleGrantedAuthority(role));
        }
        for (String scope : scopes) {
            authorities.add(new SimpleGrantedAuthority(scope));
        }
        return jwt().authorities(authorities);
    }

    /**
     * Apply JWT with roles & scopes to any request builder
     *
     * @param builder
     * @param roles
     * @param scopes
     * @return
     */
    protected MockHttpServletRequestBuilder withAuth(MockHttpServletRequestBuilder builder, List<String> roles, List<String> scopes) {
        return builder.with(jwtWith(roles, scopes));
    }

    /**
     * Convenience methods for HTTP verbs with auth
     *
     * @param url
     * @param roles
     * @param scopes
     * @return
     */
    protected MockHttpServletRequestBuilder getAuth(String url, List<String> roles, List<String> scopes) {
        var builder = get(url);
        return withAuth(builder, roles, scopes);
    }

    protected MockHttpServletRequestBuilder postAuth(String url, Object body, List<String> roles, List<String> scopes) {
        var builder = post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJson(body));
        return withAuth(builder, roles, scopes);
    }

    protected MockHttpServletRequestBuilder putAuth(String url, Object body, List<String> roles, List<String> scopes) {
        var builder = put(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJson(body));
        return withAuth(builder, roles, scopes);
    }

    protected MockHttpServletRequestBuilder deleteAuth(String url, List<String> roles, List<String> scopes) {
        var builder = delete(url);
        return withAuth(builder, roles, scopes);
    }

    /**
     * Convenience for multipart/form-data requests with auth
     *
     * @param url
     * @param file
     * @param roles
     * @param scopes
     * @return
     */
    protected MockHttpServletRequestBuilder multipartAuth(String url, MockMultipartFile file, List<String> roles, List<String> scopes) {
        var builder = multipart(url)
                .file(file)
                .contentType(MediaType.MULTIPART_FORM_DATA);
        return withAuth(builder, roles, scopes);
    }
}
