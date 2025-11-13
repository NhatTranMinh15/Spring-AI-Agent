package com.agent_java.orchestrator.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "conversation")
@Data
@EqualsAndHashCode(callSuper = true)
public class ConversationEntity extends BaseEntity {

    @Column(columnDefinition = "TEXT")
    String title;
    String username;
    @Column(name = "is_active")
    boolean isActive = true;
    @OneToMany(mappedBy = "conversation", fetch = FetchType.LAZY, cascade = {CascadeType.ALL}, orphanRemoval = true)
    List<ChatMessageEntity> messages = new ArrayList<>();

    public ConversationEntity() {
    }

    public ConversationEntity(String title, String username) {
        this.title = title;
        this.username = username;
    }

}
