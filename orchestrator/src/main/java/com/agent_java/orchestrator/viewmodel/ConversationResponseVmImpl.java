package com.agent_java.orchestrator.viewmodel;

import java.time.Instant;
import java.util.UUID;

public record ConversationResponseVmImpl(UUID id, String title, Instant createdAt) implements ConversationResponseVm {

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public Instant getCreatedAt() {
        return createdAt;
    }

}
