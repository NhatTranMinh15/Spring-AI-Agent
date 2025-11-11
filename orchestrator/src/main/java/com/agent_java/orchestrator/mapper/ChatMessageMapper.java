package com.agent_java.orchestrator.mapper;

import com.agent_java.orchestrator.viewmodel.ChatMessageResponseVm;

public class ChatMessageMapper {

    public static String toHistoryFormat(ChatMessageResponseVm entity) {
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
}
