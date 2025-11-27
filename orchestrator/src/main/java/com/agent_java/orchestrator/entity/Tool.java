package com.agent_java.orchestrator.entity;

import com.agent_java.orchestrator.entity.agent.AgentTool;
import com.agent_java.orchestrator.entity.base.SoftDeletableEntity;
import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "tool")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Tool extends SoftDeletableEntity {

    @Column(nullable = false, length = 100)
    String name;

    @Column(length = 50)
    String type = null; // e.g., "search", "retrieval", "custom"

    @Column(columnDefinition = "TEXT")
    String description = null;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    Map<String, Object> config = null;

    @OneToMany(mappedBy = "tool", cascade = {CascadeType.ALL}, orphanRemoval = true)
    Set<AgentTool> agents = new HashSet();

    public Tool(String name, String type, String description, Map<String, Object> config) {
        this.name = name;
        this.type = type;
        this.description = description;
        this.config = config;
    }

}
