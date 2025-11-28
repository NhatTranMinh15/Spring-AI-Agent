package com.agent_java.orchestrator.component;

import com.agent_java.orchestrator.dto.ToolRequestDto;
import com.agent_java.orchestrator.mapper.ToolMapper;
import com.agent_java.orchestrator.repository.ToolRepository;
import com.agent_java.orchestrator.utils.Utils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.stream.Collectors;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import java.util.Map;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.ai.tool.ToolCallback;

@Component
public class AppListener {

    private final ToolCallbackProvider toolCallbackProvider;
    private final ToolRepository toolRepo;
    private final ObjectMapper objectMapper;

    @Autowired
    public AppListener(ToolCallbackProvider toolCallbackProvider, ToolRepository toolRepo, ObjectMapper objectMapper) {
        this.toolCallbackProvider = toolCallbackProvider;
        this.toolRepo = toolRepo;
        this.objectMapper = objectMapper;
    }

    @EventListener(ApplicationReadyEvent.class)
    private void onReady() throws JsonProcessingException {
        initializeTool();
    }

    private void initializeTool() throws JsonProcessingException {
        var allToolEntities = toolRepo.findAll();
        var toolCallbacks = toolCallbackProvider.getToolCallbacks();
        if (toolCallbacks.length == 0 && allToolEntities.isEmpty()) {
            return;
        }

        var activeTools = allToolEntities.stream().filter(tool -> tool.isActive()).collect(Collectors.toList());

        // Handle case add new tools if not exist
        if (toolCallbacks.length > 0) {
            for (ToolCallback callback : toolCallbacks) {
                var toolDefinition = callback.getToolDefinition();
                String shortName = Utils.getShortToolName(toolDefinition.name());
                var toolName = shortName != null ? shortName : "";
                var tool = allToolEntities.stream().filter((t) -> t.getName().equals(toolName)).findFirst();
                if (tool.isEmpty()) {
                    var type = new TypeReference<Map<String, Object>>() {
                    };
                    Map<String, Object> config = objectMapper.readValue(toolDefinition.inputSchema(), type);
                    var toolEntity = ToolMapper.toEntity(
                            new ToolRequestDto(
                                    toolName,
                                    config.get("type").toString(),
                                    toolDefinition.description(),
                                    config)
                    );
                    toolRepo.save(toolEntity);
                }
            }
        }
        // Handle case don't have any tools are registered in MCP => Inactive all tool in DB
        if (toolCallbacks.length == 0 && !activeTools.isEmpty()) {
            activeTools.forEach((t) -> t.markDeleted());
            toolRepo.saveAll(activeTools);
        }
    }
}
