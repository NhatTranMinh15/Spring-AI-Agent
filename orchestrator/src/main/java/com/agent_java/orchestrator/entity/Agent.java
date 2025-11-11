package com.agent_java.orchestrator.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Map;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "agent", indexes = {
    @Index(name = "idx_agent_name", columnList = "name"),
    @Index(name = "idx_agent_model", columnList = "model"),
    @Index(name = "idx_agent_provider", columnList = "provider"),
    @Index(name = "idx_agent_active", columnList = "active"),
    @Index(name = "idx_agent_deleted_at", columnList = "deleted_at"),}
)
@Data
@EqualsAndHashCode(callSuper = true)
public class Agent extends BaseEntity {
    
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
    
    @Column(nullable = false, length = 100)
    String name;
    
    @Column(nullable = false, length = 100)
    String model;
    
    @Column(columnDefinition = "TEXT")
    String description;
    
    @Column(nullable = false, precision = 3, scale = 2)
    BigDecimal temperature = DEFAULT_TEMPERATURE;
    
    @Column(name = "max_tokens", nullable = false)
    int maxTokens;
    
    @Column(name = "top_p", nullable = false, precision = 3, scale = 2)
    BigDecimal topP;
    
    @Column(name = "frequency_penalty", nullable = false, precision = 3, scale = 2)
    BigDecimal frequencyPenalty;
    
    @Column(name = "presence_penalty", nullable = false, precision = 3, scale = 2)
    BigDecimal presencePenalty;
    
    @Column(nullable = false)
    boolean active = true;
    
    @Column(length = 50)
    String provider;
    
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    Map<String, Object> settings;
    
    @Version
    @Column(nullable = false)
    int version = 0;
    
    @Column(name = "deleted_at")
    ZonedDateTime deletedAt = null;
    
    public Agent(String name, String model, String description, BigDecimal temperature, int maxTokens, BigDecimal topP, BigDecimal frequencyPenalty, BigDecimal presencePenalty, boolean active, String provider, Map<String, Object> settings) {
        this.name = name;
        this.model = model;
        this.description = description;
        this.temperature = temperature;
        this.maxTokens = maxTokens;
        this.topP = topP;
        this.frequencyPenalty = frequencyPenalty;
        this.presencePenalty = presencePenalty;
        this.active = active;
        this.provider = provider;
        this.settings = settings;
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
