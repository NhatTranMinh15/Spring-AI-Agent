package com.agent_java.mcp_server.viewmodel;

import jakarta.annotation.Nullable;

public record GoogleSearchResponseVm(
        @Nullable
        String kind,
        @Nullable
        String title,
        @Nullable
        String htmlTitle,
        @Nullable
        String link,
        @Nullable
        String displayLink,
        @Nullable
        String snippet,
        @Nullable
        String htmlSnippet,
        @Nullable
        String formattedUrl,
        @Nullable
        String htmlFormattedUrl,
        @Nullable
        Object pagemap) {

}
