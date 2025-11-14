package com.agent_java.orchestrator.mapper;

import com.agent_java.orchestrator.entity.ChatMessageEntity;
import com.agent_java.orchestrator.viewmodel.ChatMessageMediaVm;
import com.agent_java.orchestrator.viewmodel.ChatMessageResponseVm;
import java.util.Base64;

public class ChatMessageMapper {

    public static String toHistoryFormat(ChatMessageEntity entity) {
        String role = switch (entity.getType()) {
            case 1 ->
                "User";
            case 2 ->
                "Assistant";
            default ->
                "Unknown";
        };
        return role + ": " + entity.getContent();
    }

    public static ChatMessageResponseVm toResponse(ChatMessageEntity chatMessage) {
        var media = chatMessage.getMessageMedias().stream().map((mm) -> {
            var base64 = Base64.getEncoder().encodeToString(mm.getData());
            return new ChatMessageMediaVm(mm.getFileName(), mm.getContentType(), "data:" + mm.getContentType() + ";base64," + base64);
        }).toList();
        return new ChatMessageResponseVm(
                chatMessage.getId(),
                chatMessage.getContent(),
                chatMessage.getCreatedAt(),
                chatMessage.getType(),
                media
        );
    }
}
