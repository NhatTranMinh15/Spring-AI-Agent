package com.agent_java.orchestrator.repository;

import com.agent_java.orchestrator.entity.ChatMessageEntity;
import com.agent_java.orchestrator.viewmodel.ChatMessageResponseVm;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessageEntity, UUID> {

    @Query("SELECT m.id AS id,m.content AS content,m.createdAt AS createdAt,m.type AS type FROM ChatMessageEntity m WHERE m.conversation.id = :conversationId ORDER BY m.createdAt ASC")
    List<ChatMessageResponseVm> listMessageByConversationId(@Param("conversationId") UUID conversationId);
}
