package com.agent_java.orchestrator.component;

import com.agent_java.orchestrator.utils.Utils;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.stereotype.Component;

@Component
public class FilteredToolCallbackProvider {

    public List<ToolCallback> filterCallbacksByToolNames(
            ToolCallback[] callbacks,
            List<String> allowedTools
    ) {
        if (allowedTools == null || allowedTools.isEmpty()) {
            return new ArrayList<>();
        }

        return Arrays.stream(callbacks)
                .filter(Objects::nonNull)
                .filter(cb -> {
                    var toolName = Utils.getShortToolName(cb.getToolDefinition().name());
                    return toolName != null && allowedTools.contains(toolName);
                })
                .collect(Collectors.toList());
    }
}
