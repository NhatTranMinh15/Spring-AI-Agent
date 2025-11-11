package com.agent_java.orchestrator.viewmodel;

import java.time.ZonedDateTime;
import java.util.UUID;

public record ConversationResponseVmImpl(UUID id, String title, ZonedDateTime createdAt) implements ConversationResponseVm {

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

}
