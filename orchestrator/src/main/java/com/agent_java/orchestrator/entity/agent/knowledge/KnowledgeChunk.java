package com.agent_java.orchestrator.entity.agent.knowledge;

import com.agent_java.orchestrator.entity.base.BaseEntity;
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
@Table(name = "knowledge_chunk")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class KnowledgeChunk extends BaseEntity {

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

    @Column(name = "embedding_768")
    @JdbcTypeCode(SqlTypes.VECTOR)
    float[] embedding768;

    @Column(name = "embedding_1536")
    @JdbcTypeCode(SqlTypes.VECTOR)
    float[] embedding1536;

    public KnowledgeChunk(AgentKnowledge knowledge, Integer chunkOrder, String content, Map<String, Object> metadata, float[] embedding768, float[] embedding1536) {
        this.knowledge = knowledge;
        this.chunkOrder = chunkOrder;
        this.content = content;
        this.metadata = metadata;
        this.embedding768 = embedding768;
        this.embedding1536 = embedding1536;
    }
}
