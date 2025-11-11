package com.agent_java.orchestrator.repository;

import com.agent_java.orchestrator.entity.Agent;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
@Repository
public interface AgentRepository extends JpaRepository<Agent, UUID> {

    public Optional<Agent> findByNameIgnoreCase(String name);

    @Query("SELECT a FROM Agent a WHERE a.deletedAt IS NULL AND a.active = true")
    public List<Agent> findAllActive();

    @Query("SELECT a FROM Agent a WHERE a.deletedAt IS NULL")
    public List<Agent> findAllNotDeleted();

    @Query("SELECT a FROM Agent a WHERE a.id = :id AND a.deletedAt IS NULL")
    public Optional<Agent> findByIdNotDeleted(UUID id);
}
