package com.agent_java.orchestrator.repository;

import com.agent_java.orchestrator.entity.ChatMessageEntity;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessageEntity, UUID> {

    @Query("SELECT m "
            + "FROM ChatMessageEntity m "
            + "LEFT JOIN FETCH m.messageMedias "
            + "WHERE m.conversation.id = :conversationId "
            + "ORDER BY m.createdAt ASC")
    List<ChatMessageEntity> listMessageByConversationId(@Param("conversationId") UUID conversationId);
}
