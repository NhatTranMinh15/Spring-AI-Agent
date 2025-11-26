package com.agent_java.orchestrator.integration.config;

import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestToolCallbackConfig {

    @Bean
    public ToolCallbackProvider toolCallbackProvider() {
        // Provide a dummy implementation — it returns no tools
        return () -> new ToolCallback[0];
    }
}
