package com.agent_java.orchestrator.viewmodel;

import java.time.Instant;
import java.util.UUID;

public interface ConversationResponseVm {

    UUID getId();

    String getTitle();

    Instant getCreatedAt();
}
