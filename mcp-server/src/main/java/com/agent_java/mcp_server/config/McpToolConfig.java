package com.agent_java.mcp_server.config;

import com.agent_java.mcp_server.service.DatetimeTool;
import com.agent_java.mcp_server.service.SearchOnlineTool;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class McpToolConfig {

    @Bean
    public ToolCallbackProvider toolProvider(DatetimeTool datetimeTool, SearchOnlineTool searchOnlineTool) {
        return MethodToolCallbackProvider
                .builder()
                .toolObjects(datetimeTool, searchOnlineTool)
                .build();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
    
    @Bean
    public ObjectMapper objectMapper(){
        return new ObjectMapper();
    }
}
