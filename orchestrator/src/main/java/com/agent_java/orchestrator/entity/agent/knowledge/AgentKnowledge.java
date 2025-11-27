package com.agent_java.orchestrator.entity.agent.knowledge;

import com.agent_java.orchestrator.entity.agent.Agent;
import com.agent_java.orchestrator.entity.base.SoftDeletableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "agent_knowledge")
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class AgentKnowledge extends SoftDeletableEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agent_id", nullable = false)
    Agent agent;

    @Column(nullable = false, length = 100)
    String name;

    @Column(name = "source_type", length = 50)
    String sourceType = null; // e.g., "url", "pdf", "repo", "dataset"

    @Column(name = "source_uri")
    String sourceUri = null;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    Map<String, Object> metadata = null;

    @Column(name = "embedding_model", length = 100)
    String embeddingModel = null;

    public AgentKnowledge(String name) {
        this.name = name;
    }

}
