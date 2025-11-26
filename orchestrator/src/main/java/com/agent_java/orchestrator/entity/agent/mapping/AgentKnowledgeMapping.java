package com.agent_java.orchestrator.entity.agent.mapping;

import com.agent_java.orchestrator.entity.base.BaseEntity;
import com.agent_java.orchestrator.entity.agent.Agent;
import com.agent_java.orchestrator.entity.agent.knowledge.AgentKnowledge;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(
        name = "agent_knowledge_mapping",
        uniqueConstraints = {
            @UniqueConstraint(columnNames = {"agent_id", "knowledge_id"})
        }
)
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AgentKnowledgeMapping extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agent_id", nullable = false)
    Agent agent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "knowledge_id", nullable = false)
    AgentKnowledge knowledge;

    public static AgentKnowledgeMapping of(Agent agent, AgentKnowledge knowledge) {
        return new AgentKnowledgeMapping(agent, knowledge);
    }

    @Override
    public String toString() {
        return agent.getId().toString() + " " + knowledge.getId().toString();
    }

}
