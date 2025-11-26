package com.agent_java.orchestrator.entity.agent;

import com.agent_java.orchestrator.entity.base.SoftDeletableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "agent_tool")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AgentTool extends SoftDeletableEntity {

    @Column(nullable = false, length = 100)
    String name;

    @Column(length = 50)
    String type = null; // e.g., "search", "retrieval", "custom"

    @Column(columnDefinition = "TEXT")
    String description = null;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    Map<String, Object> config = null;

    public AgentTool(String name) {
        this.name = name;
    }
    
    
}
