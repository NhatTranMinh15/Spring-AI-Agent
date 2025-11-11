package com.agent_java.orchestrator.viewmodel;

import java.time.ZonedDateTime;
import java.util.UUID;

public interface ChatMessageResponseVm {

    UUID getUUID();

    String getContent();

    ZonedDateTime getCreatedAt();

    int getType();
}
