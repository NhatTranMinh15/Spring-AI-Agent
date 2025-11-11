package com.agent_java.orchestrator.service;

import jakarta.annotation.Nullable;
import java.util.List;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatModelService {

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
    public String call(String message) {
        return call(message,List.of());
    }
    
    @Nullable
    public String call(String message, List<String> history) {
        StringBuilder sb = new StringBuilder();
        for (String h : history) {
            sb.append(h).append("\n");
        }
        String combinedPrompt = sb.append("User: ").append(message).toString();
        var chatClient = ChatClient.builder(chatModel).build();
        var response = chatClient
                .prompt()
                .advisors(qaAdvisor)
                .toolCallbacks(mcpClientToolCallbackProvider)
                .user(combinedPrompt)
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
