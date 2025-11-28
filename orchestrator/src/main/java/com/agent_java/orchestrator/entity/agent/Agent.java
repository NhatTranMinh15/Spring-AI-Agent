package com.agent_java.orchestrator.entity.agent;

import com.agent_java.orchestrator.entity.agent.knowledge.AgentKnowledge;
import com.agent_java.orchestrator.entity.base.SoftDeletableEntity;
import com.agent_java.orchestrator.utils.Constant;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "agent")
@Data
@EqualsAndHashCode(callSuper = true)
public class Agent extends SoftDeletableEntity {

    public static final BigDecimal DEFAULT_TEMPERATURE = new BigDecimal("0.7");
    public static final int DEFAULT_MAX_TOKENS = 2048;
    public static final BigDecimal DEFAULT_TOP_P = new BigDecimal("1.0");
    public static final BigDecimal DEFAULT_FREQUENCY_PENALTY = new BigDecimal("0.0");
    public static final BigDecimal DEFAULT_PRESENCE_PENALTY = new BigDecimal("0.0");

    public static final int MIN_MAX_TOKENS = 1;
    public static final int MAX_MAX_TOKENS = 16_384;
    public static final String MIN_TEMPERATURE = "0.0"; // change in AgentRequestDto too
    public static final String MAX_TEMPERATURE = "2.0"; // change in AgentRequestDto too
    public static final String MIN_TOP_P = "0.0";
    public static final String MAX_TOP_P = "1.0";
    public static final String MIN_PENALTY = "-2.0";
    public static final String MAX_PENALTY = "2.0";
    public static final long MIN_DIMENSION = 64;

    @Column(nullable = false, length = 100)
    String name;

    @Column(nullable = false, length = 100)
    String model;

    @Column(columnDefinition = "TEXT")
    String description;

    /**
     * Controls randomness of model output.
     * 0.0 = deterministic, 2.0 = very creative.
     */
    @Column(nullable = false, precision = 3, scale = 2)
    BigDecimal temperature = DEFAULT_TEMPERATURE;

    /**
     * Maximum number of tokens the model may generate in a single response.
     * Depends on model capacity (GPT-4o supports up to 16k).
     */
    @Column(name = "max_tokens", nullable = false)
    int maxTokens = DEFAULT_MAX_TOKENS;

    /**
     * Nucleus sampling probability cutoff.
     * 1.0 means all tokens are considered.
     */
    @Column(name = "top_p", nullable = false, precision = 3, scale = 2)
    BigDecimal topP = DEFAULT_TOP_P;

    /**
     * Penalizes new tokens based on their frequency in the text so far.
     * Range: -2.0 (encourage repetition) to 2.0 (strongly discourage repetition).
     */
    @Column(name = "frequency_penalty", nullable = false, precision = 3, scale = 2)
    BigDecimal frequencyPenalty = DEFAULT_FREQUENCY_PENALTY;

    /**
     * Penalizes new tokens if they already appear in the text.
     * Range: -2.0 (encourage reuse) to 2.0 (discourage reuse).
     */
    @Column(name = "presence_penalty", nullable = false, precision = 3, scale = 2)
    BigDecimal presencePenalty = DEFAULT_PRESENCE_PENALTY;

    @Column(length = 50)
    String provider = null;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    Map<String, Object> settings;

    @Column(name = "base_url", length = 150, nullable = false)
    String baseUrl;

    @Column(name = "api_key", length = 200, nullable = false)
    String apiKey;

    @Column(name = "chat_completions_path", length = 50, nullable = false)
    String chatCompletionsPath;

    @Column(name = "embeddings_path", length = 50, nullable = false)
    String embeddingsPath;

    @Column(name = "embedding_model", length = 50, nullable = false)
    String embeddingModel;

    @Column(nullable = false)
    int dimension = Constant.CHATGPT_DIMENSION;

    @Version
    @Column(nullable = false)
    int version = 0;

    @OneToMany(mappedBy = "agent", cascade = {CascadeType.ALL}, orphanRemoval = true)
    Set<AgentTool> tools = new HashSet();

    @OneToMany(mappedBy = "agent", cascade = {CascadeType.ALL}, orphanRemoval = true)
    Set<AgentKnowledge> knowledge = new HashSet();

    public Agent() {
    }

    public Agent(String name, String model, String description, BigDecimal temperature, int maxTokens, BigDecimal topP, BigDecimal frequencyPenalty, BigDecimal presencePenalty, String provider, Map<String, Object> settings) {
        this(name, model);
        this.description = description;
        this.temperature = temperature;
        this.maxTokens = maxTokens;
        this.topP = topP;
        this.frequencyPenalty = frequencyPenalty;
        this.presencePenalty = presencePenalty;
        this.provider = provider;
        this.settings = settings;
    }

    public Agent(String name, String model) {
        this.name = name;
        this.model = model;
    }

    public Agent(String name, String model, String description, BigDecimal temperature, int maxTokens, BigDecimal topP, BigDecimal frequencyPenalty, BigDecimal presencePenalty, String provider, Map<String, Object> settings, String baseUrl, String apiKey, String chatCompletionsPath, String embeddingsPath, String embeddingModel, int dimension) {
        this(name, model, description, temperature, maxTokens, topP, frequencyPenalty, presencePenalty, provider, settings);
        this.baseUrl = baseUrl;
        this.apiKey = apiKey;
        this.chatCompletionsPath = chatCompletionsPath;
        this.embeddingsPath = embeddingsPath;
        this.embeddingModel = embeddingModel;
        this.dimension = dimension;
    }

    public void setTemperature(double temperature) {
        this.temperature = new BigDecimal(temperature);
    }

    public void setTopP(double topP) {
        this.topP = new BigDecimal(topP);
    }

    public void setFrequencyPenalty(double frequencyPenalty) {
        this.frequencyPenalty = new BigDecimal(frequencyPenalty);
    }

    public void setPresencePenalty(double presencePenalty) {
        this.presencePenalty = new BigDecimal(presencePenalty);
    }

}
