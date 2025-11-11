package com.agent_java.mcp_server.viewmodel;

import jakarta.annotation.Nullable;
import java.time.ZonedDateTime;

public class ErrorResponseVm {

    int status;
    String error;
    
    @Nullable
    String message;
    
    ZonedDateTime timestamp;

    public ErrorResponseVm(int status, String error) {
        this(status, error, null);
    }

    public ErrorResponseVm(int status, String error, String message) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.timestamp = ZonedDateTime.now();
    }

}
