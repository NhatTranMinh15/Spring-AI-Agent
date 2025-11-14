package com.agent_java.orchestrator.repository;

import com.agent_java.orchestrator.entity.ChatMessageMediaEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMessageMediaRepository extends JpaRepository<ChatMessageMediaEntity, UUID> {

}
