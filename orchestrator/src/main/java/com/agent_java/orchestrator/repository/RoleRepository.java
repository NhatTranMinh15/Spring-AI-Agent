package com.agent_java.orchestrator.repository;

import com.agent_java.orchestrator.entity.RoleEntity;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<RoleEntity, UUID> {

    boolean existsByName(String name);

    Optional<RoleEntity> findByName(String name);

    List<RoleEntity> findByNameIn(List<String> names);
}
