package com.agent_java.orchestrator.repository;

import com.agent_java.orchestrator.entity.SystemSettingEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SystemSettingRepository extends JpaRepository<SystemSettingEntity, UUID>{

}
