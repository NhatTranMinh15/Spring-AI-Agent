package com.agent_java.orchestrator.viewmodel;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import java.util.UUID;
import lombok.Data;

@Data
public class ChatRequestVm {

    @Nullable
    UUID conversationId;

    @NotBlank(message = "Question must not be blank")
    String question;

}
