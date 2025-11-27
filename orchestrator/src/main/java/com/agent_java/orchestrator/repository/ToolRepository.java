package com.agent_java.orchestrator.repository;

import com.agent_java.orchestrator.entity.Tool;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ToolRepository extends JpaRepository<Tool, UUID> {

    public List<Tool> findAllByActiveTrue();
}
