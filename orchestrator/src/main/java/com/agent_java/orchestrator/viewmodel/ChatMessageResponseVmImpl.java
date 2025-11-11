package com.agent_java.orchestrator.viewmodel;

import java.time.ZonedDateTime;
import java.util.UUID;

public record ChatMessageResponseVmImpl(UUID id, String content, ZonedDateTime createdAt, int type) implements ChatMessageResponseVm {

    @Override
    public UUID getUUID() {
        return id;
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public int getType() {
        return type;
    }

}
