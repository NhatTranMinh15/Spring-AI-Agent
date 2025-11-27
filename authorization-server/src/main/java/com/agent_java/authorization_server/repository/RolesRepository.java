package com.agent_java.authorization_server.repository;

import com.agent_java.authorization_server.entity.RolesEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolesRepository extends JpaRepository<RolesEntity, UUID> {

    public Optional<RolesEntity> findByName(String name);
}
