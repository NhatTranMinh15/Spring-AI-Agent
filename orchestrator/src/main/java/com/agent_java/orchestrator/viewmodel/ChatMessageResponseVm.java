package com.agent_java.orchestrator.viewmodel;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ChatMessageResponseVm(
        UUID uuid,
        String content,
        Instant createdAt,
        int type,
        List<ChatMessageMediaVm> media) {

}
