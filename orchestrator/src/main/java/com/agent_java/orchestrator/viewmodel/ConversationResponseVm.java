package com.agent_java.orchestrator.viewmodel;

import java.time.OffsetDateTime;
import java.util.UUID;

public interface ConversationResponseVm {

    UUID getId();

    String getTitle();

    OffsetDateTime getCreatedAt();
}
