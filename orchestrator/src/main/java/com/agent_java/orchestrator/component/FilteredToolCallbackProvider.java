package com.agent_java.orchestrator.component;

import java.util.*;
import java.util.stream.Collectors;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.definition.ToolDefinition;
import org.springframework.stereotype.Component;

@Component
public class FilteredToolCallbackProvider {

    public List<ToolCallback> filterCallbacksByToolNames(
            ToolCallback[] callbacks,
            List<String> allowedTools
    ) {
        if (allowedTools == null || allowedTools.isEmpty()) {
            return Collections.emptyList();
        }

        return Arrays.stream(callbacks)
                .filter(Objects::nonNull)
                .filter(cb -> {
                    String toolName = Optional.ofNullable(cb.getToolDefinition())
                            .map(ToolDefinition::name)
                            .filter(name -> !name.isBlank())
                            .map(name -> {
                                String afterLast = name.substring(name.lastIndexOf("_") + 1);
                                return afterLast.isBlank() ? null : afterLast;
                            })
                            .orElse(null);

                    return toolName != null && allowedTools.contains(toolName);
                })
                .collect(Collectors.toList());
    }
}
