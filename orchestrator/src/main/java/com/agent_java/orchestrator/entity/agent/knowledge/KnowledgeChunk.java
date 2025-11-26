package com.agent_java.orchestrator.entity.agent.knowledge;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@SuppressWarnings("LongParameterList")
@Entity
@Table(name = "knowledge_chunk")
@Data
@NoArgsConstructor
public class KnowledgeChunk {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    UUID id = null;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "knowledge_id", nullable = false)
    AgentKnowledge knowledge;

    @Column(name = "chunk_order", nullable = false)
    int chunkOrder = 0;

    @Column(nullable = false, columnDefinition = "TEXT")
    String content;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    Map<String, Object> metadata = null;

    @Column(name = "embedding")
    @JdbcTypeCode(SqlTypes.VECTOR)
    float[] embedding;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false, nullable = false)
    OffsetDateTime createdAt = null;

    public KnowledgeChunk(AgentKnowledge knowledge, Integer chunkOrder, String content, Map<String, Object> metadata, float[] embedding) {
        this.knowledge =knowledge;
        this.chunkOrder = chunkOrder;
        this.content = content;
        this.metadata = metadata;
        this.embedding = embedding;
    }
}
