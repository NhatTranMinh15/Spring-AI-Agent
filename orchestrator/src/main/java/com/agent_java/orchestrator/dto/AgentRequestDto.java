package com.agent_java.orchestrator.dto;

import com.agent_java.orchestrator.entity.agent.Agent;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgentRequestDto {

    @NotBlank(message = "Agent name must not be blank")
    @Size(max = 100, message = "Agent name must not exceed 100 characters")
    String name;

    String description = null;

    boolean active = true;

    String provider;

    @NotBlank(message = "Base url must not be blank")
    String baseUrl;

    @NotBlank(message = "Api key must not be blank")
    String apiKey;

    @NotBlank(message = "Chat completions path must not be blank")
    String chatCompletionsPath;

    @NotBlank(message = "Model must not be blank")
    @Size(max = 100, message = "Model name must not exceed 100 characters")
    String model;

    @NotBlank(message = "Embedding model must not be blank")
    String embeddingModel;

    @Min(value = Agent.MIN_DIMENSION, message = "Min embedding must be at least {value}")
    int dimension;

    @NotBlank(message = "Embeddings path must not be blank")
    String embeddingsPath;

    @DecimalMin(value = Agent.MIN_TOP_P, inclusive = true, message = "TopP must be at least {value}")
    @DecimalMax(value = Agent.MAX_TOP_P, inclusive = true, message = "TopP must not exceed {value}")
    double topP = Agent.DEFAULT_TOP_P.doubleValue();

    @DecimalMin(value = Agent.MIN_TEMPERATURE, inclusive = true, message = "Temperature must be at least {value}")
    @DecimalMax(value = Agent.MAX_TEMPERATURE, inclusive = true, message = "Temperature must not exceed {value}")
    double temperature = Agent.DEFAULT_TEMPERATURE.doubleValue();

    @Min(value = Agent.MIN_MAX_TOKENS, message = "Max tokens must be at least {value}")
    @Max(value = Agent.MAX_MAX_TOKENS, message = "Max tokens must not exceed {value}")
    int maxTokens = Agent.DEFAULT_MAX_TOKENS;

    @DecimalMin(value = Agent.MIN_PENALTY, inclusive = true, message = "Frequency penalty must be at least {value}")
    @DecimalMax(value = Agent.MAX_PENALTY, inclusive = true, message = "Frequency penalty must not exceed {value}")
    double frequencyPenalty = Agent.DEFAULT_FREQUENCY_PENALTY.doubleValue();

    @DecimalMin(value = Agent.MIN_PENALTY, inclusive = true, message = "Presence penalty must be at least {value}")
    @DecimalMax(value = Agent.MAX_PENALTY, inclusive = true, message = "Presence penalty must not exceed {value}")
    double presencePenalty = Agent.DEFAULT_PRESENCE_PENALTY.doubleValue();

    Map<String, Object> settings;
}
