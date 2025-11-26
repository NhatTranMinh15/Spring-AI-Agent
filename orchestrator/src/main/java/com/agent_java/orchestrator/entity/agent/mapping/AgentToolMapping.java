package com.agent_java.orchestrator.entity.agent.mapping;

import com.agent_java.orchestrator.entity.base.BaseEntity;
import com.agent_java.orchestrator.entity.agent.Agent;
import com.agent_java.orchestrator.entity.agent.AgentTool;
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
        name = "agent_tool_mapping",
        uniqueConstraints = {
            @UniqueConstraint(columnNames = {"agent_id", "tool_id"})
        }
)
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AgentToolMapping extends BaseEntity{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agent_id", nullable = false)
    Agent agent;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tool_id", nullable = false)
    AgentTool tool;
    
    public static AgentToolMapping of(Agent agent, AgentTool tool){
        return new AgentToolMapping(agent, tool);
    }
}
