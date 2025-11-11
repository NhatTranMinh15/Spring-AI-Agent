package com.agent_java.orchestrator.viewmodel;

import java.time.ZonedDateTime;
import java.util.UUID;

public interface ConversationResponseVm {

    UUID getId();

    String getTitle();

    ZonedDateTime getCreatedAt();
}
