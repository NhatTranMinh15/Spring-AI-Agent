package com.agent_java.orchestrator.viewmodel;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public record ChatMessageResponseVm(
        UUID uuid,
        String content,
        OffsetDateTime createdAt,
        int type,
        List<ChatMessageMediaVm> media) {

}
