package com.agent_java.orchestrator.viewmodel;

import java.time.OffsetDateTime;
import java.util.UUID;

public record ConversationResponseVmImpl(UUID id, String title, OffsetDateTime createdAt) implements ConversationResponseVm {

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

}
