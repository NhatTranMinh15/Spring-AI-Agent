package com.agent_java.orchestrator.repository;

import com.agent_java.orchestrator.entity.Role;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, UUID> {

    boolean existsByName(String name);

    Role findByName(String name);

    List<Role> findByNameIn(List<String> names);
}
