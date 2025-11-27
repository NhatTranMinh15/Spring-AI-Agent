package com.agent_java.authorization_server.exception;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.Data;

@Data
public class ErrorResponse {

    int status;
    String message;
    String timeStamp;

    public ErrorResponse(int status, String message) {
        this.status = status;
        this.message = message;
        timeStamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

}
