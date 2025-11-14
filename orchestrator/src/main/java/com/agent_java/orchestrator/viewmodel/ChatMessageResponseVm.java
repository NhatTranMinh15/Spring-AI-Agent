package com.agent_java.orchestrator.viewmodel;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

public record ChatMessageResponseVm(
        UUID uuid,
        String content,
        ZonedDateTime createdAt,
        int type,
        List<ChatMessageMediaVm> media) {

}
