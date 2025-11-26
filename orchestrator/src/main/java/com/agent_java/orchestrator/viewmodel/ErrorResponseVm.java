package com.agent_java.orchestrator.viewmodel;

import jakarta.annotation.Nullable;
import java.time.OffsetDateTime;
import lombok.Data;

@Data
public class ErrorResponseVm {

    int status;

    String error;

    @Nullable
    String message;

    OffsetDateTime timestamp = OffsetDateTime.now();
}
