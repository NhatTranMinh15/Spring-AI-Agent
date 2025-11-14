package com.agent_java.orchestrator.service;

import com.agent_java.orchestrator.utils.Constant;
import com.agent_java.orchestrator.viewmodel.ChatRequestVm;
import jakarta.annotation.Nullable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

@Service
public class ChatModelService {

    private final Logger logger = LoggerFactory.getLogger(ChatModelService.class);

    ChatModel chatModel;
    QuestionAnswerAdvisor qaAdvisor;
    ToolCallbackProvider mcpClientToolCallbackProvider;

    @Autowired
    public ChatModelService(ChatModel chatModel, QuestionAnswerAdvisor qaAdvisor, ToolCallbackProvider mcpClientToolCallbackProvider) {
        this.chatModel = chatModel;
        this.qaAdvisor = qaAdvisor;
        this.mcpClientToolCallbackProvider = mcpClientToolCallbackProvider;
    }

    @Nullable
    public String call(ChatRequestVm request) {
        return call(request, new ArrayList<>());
    }

    @Nullable
    public String call(ChatRequestVm request, List<String> history) {
        StringBuilder sb = new StringBuilder();
        for (String h : history) {
            sb.append(h).append("\n");
        }
        String combinedPrompt = sb.append("User: ").append(request.question()).toString();
        var chatClient = ChatClient.builder(chatModel).build();
        var response = chatClient
                .prompt()
                .advisors(qaAdvisor)
                .toolCallbacks(mcpClientToolCallbackProvider)
                .user((u) -> {
                    u.text(combinedPrompt);
                    var files = request.files();
                    if (files != null) {
                        files.stream()
                                .filter((t) -> !t.isEmpty())
                                .forEach((file) -> {
                                    try {
                                        String type = file.getContentType() != null ? file.getContentType() : Constant.PNG_CONTENT_TYPE;
                                        var mime = MimeTypeUtils.parseMimeType(type);
                                        var resource = new InputStreamResource(file.getInputStream());
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

    public String createSummarize(String question) {
        var prompt = new Prompt(
                """
                ${Constant.SUMMARY_PROMPT}
                "$question"
"""); // dont need to Kotlin trimIndent()
        var response = chatModel.call(prompt);
        return response.getResult().getOutput().getText();
    }

}
