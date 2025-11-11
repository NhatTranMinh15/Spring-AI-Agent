package com.agent_java.orchestrator.repository;

import com.agent_java.orchestrator.entity.ConversationEntity;
import com.agent_java.orchestrator.viewmodel.ConversationResponseVm;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ConversationRepository extends JpaRepository<ConversationEntity, UUID> {

    @Query("SELECT c.id AS id, c.title AS title, c.createdAt AS createdAt FROM ConversationEntity c WHERE c.username = :userName AND c.isActive = true ORDER BY c.createdAt DESC")
    List<ConversationResponseVm> listActiveConversationsByUser(@Param("userName") String userName);
}
