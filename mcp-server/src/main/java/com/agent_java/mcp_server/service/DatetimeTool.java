package com.agent_java.mcp_server.service;

import com.agent_java.mcp_server.utils.Constant;
import org.springframework.ai.chat.messages.ToolResponseMessage;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
public class DatetimeTool {

    public static DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(Constant.FULL_DATETIME_FORMAT);

    @Tool(description = "Return current datetime in UTC format")
    public ToolResponseMessage.ToolResponse getCurrentDatetime() {
        var now = LocalDateTime.now(ZoneOffset.UTC);
        return new ToolResponseMessage.ToolResponse(
                UUID.randomUUID().toString(),
                "Result of getCurrentDatetime tool",
                now.format(FORMATTER)
        );
    }
}
