package com.agent_java.orchestrator.service;

import com.agent_java.orchestrator.repository.AgentRepository;
import io.micrometer.observation.ObservationRegistry;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.document.MetadataMode;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.model.SimpleApiKey;
import org.springframework.ai.model.tool.ToolCallingManager;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.ai.openai.OpenAiEmbeddingOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestClient;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class DynamicModelService {

    private final ToolCallingManager toolCallingManager;
    private final RetryTemplate retryTemplate;
    private final ObservationRegistry observationRegistry;
    private final AgentRepository agentRepo;

    @Autowired
    public DynamicModelService(ToolCallingManager toolCallingManager, RetryTemplate retryTemplate, ObservationRegistry observationRegistry, AgentRepository agentRepo) {
        this.toolCallingManager = toolCallingManager;
        this.retryTemplate = retryTemplate;
        this.observationRegistry = observationRegistry;
        this.agentRepo = agentRepo;
    }

    private final Map<UUID, Pair<ChatModel, EmbeddingModel>> cache = new HashMap<>();

    public ChatModel getChatModel(UUID agentId) {
        if (cache.containsKey(agentId)) {
            return cache.get(agentId).getFirst();
        }
        Pair<ChatModel, EmbeddingModel> model = createModels(agentId);
        cache.put(agentId, model);
        return model.getFirst();
    }

    public EmbeddingModel getEmbeddingModel(UUID agentId) {
        if (cache.containsKey(agentId)) {
            return cache.get(agentId).getSecond();
        }
        Pair<ChatModel, EmbeddingModel> model = createModels(agentId);
        cache.put(agentId, model);
        return model.getSecond();
    }

    private Pair<ChatModel, EmbeddingModel> createModels(UUID agentId) {
        var config = agentRepo.findById(agentId).orElseThrow();
        var chatModel = createChatModel(
                config.getBaseUrl(),
                config.getApiKey(),
                config.getModel(),
                config.getChatCompletionsPath(),
                config.getEmbeddingsPath()
        );
        var embeddingModel = createEmbeddingModel(
                config.getBaseUrl(),
                config.getApiKey(),
                config.getChatCompletionsPath(),
                config.getEmbeddingsPath(),
                config.getEmbeddingModel(),
                config.getDimension()
        );
        return Pair.of(chatModel, embeddingModel);
    }

    private ChatModel createChatModel(
            String baseUrl,
            String apiKey,
            String modelName,
            String chatCompletionsPath,
            String embeddingsPath
    ) {
        var openAiApi = createOpenAiApi(baseUrl, apiKey, chatCompletionsPath, embeddingsPath);

        var options = OpenAiChatOptions.builder().model(modelName).build();

        return new OpenAiChatModel(openAiApi, options, toolCallingManager, retryTemplate, observationRegistry);
    }

    private EmbeddingModel createEmbeddingModel(
            String baseUrl,
            String apiKey,
            String chatCompletionsPath,
            String embeddingsPath,
            String embeddingModel,
            int dimension
    ) {
        var api = createOpenAiApi(baseUrl, apiKey, chatCompletionsPath, embeddingsPath);

        return new OpenAiEmbeddingModel(
                api,
                MetadataMode.EMBED,
                OpenAiEmbeddingOptions.builder().model(embeddingModel).dimensions(dimension).build()
        );
    }

    private OpenAiApi createOpenAiApi(
            String baseUrl,
            String apiKey,
            String chatCompletionsPath,
            String embeddingsPath
    ) {

        return new OpenAiApi(
                baseUrl,
                new SimpleApiKey(apiKey),
                new LinkedMultiValueMap(),
                chatCompletionsPath,
                embeddingsPath,
                RestClient.builder(),
                WebClient.builder(),
                new DefaultResponseErrorHandler()
        );
    }
}
