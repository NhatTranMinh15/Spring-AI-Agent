package com.agent_java.orchestrator.viewmodel;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChatResponseVm {

    private ConversationResponseVm conversation;

    @Nullable
    private ChatMessageResponseVm message;
}
