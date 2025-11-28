package com.agent_java.orchestrator.service;

import com.agent_java.orchestrator.component.FilteredToolCallbackProvider;
import com.agent_java.orchestrator.component.GlobalToolCallbackProvider;
import com.agent_java.orchestrator.repository.AgentKnowledgeRepository;
import com.agent_java.orchestrator.repository.AgentToolRepository;
import com.agent_java.orchestrator.utils.Constant;
import com.agent_java.orchestrator.viewmodel.ChatRequestVm;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

@Service
public class ChatModelService {

    private final Logger logger = LoggerFactory.getLogger(ChatModelService.class);

    private final VectorStoreService vectorStoreService;
    private final AgentKnowledgeRepository agentKnowledgeRepo;
    private final AgentToolRepository agentToolRepository;
    private final FilteredToolCallbackProvider filteredToolCallbackProvider;
    private final GlobalToolCallbackProvider globalToolCallbackProvider;
    private final DynamicModelService dynamicModelService;

    @Autowired
    public ChatModelService(VectorStoreService vectorStoreService, AgentKnowledgeRepository agentKnowledgeRepo, AgentToolRepository agentToolRepository, FilteredToolCallbackProvider filteredToolCallbackProvider, GlobalToolCallbackProvider globalToolCallbackProvider, DynamicModelService dynamicModelService) {
        this.vectorStoreService = vectorStoreService;
        this.agentKnowledgeRepo = agentKnowledgeRepo;
        this.agentToolRepository = agentToolRepository;
        this.filteredToolCallbackProvider = filteredToolCallbackProvider;
        this.globalToolCallbackProvider = globalToolCallbackProvider;
        this.dynamicModelService = dynamicModelService;
    }

    public String call(ChatRequestVm request) {
        return call(request, new ArrayList<>());
    }

    public String call(ChatRequestVm request, List<String> history) {
        StringBuilder sb = new StringBuilder();
        sb.append(Constant.SEARCH_TOOL_INSTRUCTION).append("\n");
        for (String h : history) {
            sb.append(h).append("\n");
        }
        sb.append("User: ").append(request.getQuestion());
        String combinedPrompt = sb.toString();

        ChatModel model = dynamicModelService.getChatModel(request.getAgentId());
        var chatClient = ChatClient.builder(model).build();
        var qaAdvisor = createQaAdvisorForAgent(request.getAgentId());

        var promptBuilder = chatClient.prompt();
        if (qaAdvisor != null) {
            promptBuilder = promptBuilder.advisors(qaAdvisor);
        }
        var response = promptBuilder
                .toolCallbacks(createToolForAgent(request.getAgentId()))
                .user((u) -> {
                    u.text(combinedPrompt);
                    var files = request.getFiles();
                    if (files != null) {
                        files.stream()
                                .filter((t) -> !t.isEmpty())
                                .forEach((file) -> {
                                    try (var in = file.getInputStream()) {
                                        String type = file.getContentType() != null ? file.getContentType() : Constant.PNG_CONTENT_TYPE;
                                        var mime = MimeTypeUtils.parseMimeType(type);
                                        var resource = new InputStreamResource(in);
                                        u.media(mime, resource);
                                    } catch (IOException ex) {
                                        logger.warn("Failed to read file " + file.getOriginalFilename() + ": " + ex.getMessage());
                                    }
                                });
                    }
                })
                .call()
                .content();
        return response;
    }

    public String createSummarize(UUID agentId, String question) {
        var prompt = new Prompt(String.format(
                """
                %s
                %s
                """,
                Constant.SUMMARY_PROMPT, question)
                .trim()
        );
        var chatModel = dynamicModelService.getChatModel(agentId);
        var response = chatModel.call(prompt);
        return response.getResult().getOutput().getText();
    }

    QuestionAnswerAdvisor createQaAdvisorForAgent(UUID agentId) {
        List<String> knowledgeIds = agentKnowledgeRepo.findAllByAgentIdAndActiveTrue(agentId).stream().map((t) -> t.getId().toString()).collect(Collectors.toList());

        if (knowledgeIds.isEmpty()) {
            return null;
        }

        String idsArray = "["
                + knowledgeIds.stream()
                        .map(id -> "\"" + id + "\"")
                        .collect(Collectors.joining(","))
                + "]";

        var searchRequest = SearchRequest.builder()
                .topK(Constant.TOP_K)
                .filterExpression("knowledge_id IN " + idsArray)
                .build();

        return QuestionAnswerAdvisor
                .builder(vectorStoreService.getVectorStore(agentId))
                .searchRequest(searchRequest)
                .build();
    }

    List<ToolCallback> createToolForAgent(UUID agentId) {
        var allowedToolNames = agentToolRepository.findByAgentId(agentId).stream().map((t) -> t.getTool().getName()).collect(Collectors.toList());

        // Get all globally available tools
        var allCallbacks = globalToolCallbackProvider.getToolCallbacks(); // Filter them based on agent-config
        return filteredToolCallbackProvider.filterCallbacksByToolNames(
                allCallbacks,
                allowedToolNames
        );
    }
}
