package com.agent_java.orchestrator.component;

import java.util.Optional;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GlobalToolCallbackProvider {
    
    @Autowired
    private ToolCallbackProvider delegate;
    
    public ToolCallback[] getToolCallbacks() {
//        return Optional.ofNullable(Arrays.asList(delegate.getToolCallbacks())).orElse(new ArrayList());
        return Optional.ofNullable(delegate.getToolCallbacks()).orElse(new ToolCallback[0]);
    }
}
