package com.agent_java.orchestrator.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "chat_message")
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ChatMessageEntity extends BaseEntity {

    @Column(columnDefinition = "TEXT")
    String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conversation_id", nullable = false)
    ConversationEntity conversation;

    int type;

    @OneToMany(mappedBy = "chatMessage", fetch = FetchType.LAZY, cascade = {CascadeType.ALL}, orphanRemoval = true)
    List<ChatMessageMediaEntity> messageMedias = new ArrayList<>();

    public ChatMessageEntity(String content, ConversationEntity conversation, int type) {
        this.content = content;
        this.conversation = conversation;
        this.type = type;
    }
}
