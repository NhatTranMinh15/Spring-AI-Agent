package com.agent_java.orchestrator.chunking;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class ChunkerProfileDetector {

    private static final int TIGHT_THRESHOLD = 2000;
    private static final int DEFAULT_THRESHOLD = 10000;
    private static final int CSV_SAMPLE_LINES = 5;

    private static Map<String, String> extensionToProfile;

    static {
        extensionToProfile = new HashMap<>();
        extensionToProfile.put("txt", "markdown");
        extensionToProfile.put("md", "markdown");
        extensionToProfile.put("pdf", "loose");
        extensionToProfile.put("docx", "loose");
        extensionToProfile.put("json", "tight");
        extensionToProfile.put("csv", "tight");
        extensionToProfile.put("xml", "tight");
        extensionToProfile.put("java", "code");
        extensionToProfile.put("kt", "code");
        extensionToProfile.put("js", "code");
        extensionToProfile.put("ts", "code");
        extensionToProfile.put("py", "code");
        extensionToProfile.put("cpp", "code");
        extensionToProfile.put("c", "code");
        extensionToProfile.put("go", "code");
    }

    public String detect(String text, String ext) {
        if (ext != null) {
            return extensionToProfile.getOrDefault(ext.toLowerCase(), "default");
        }
        return detectFromText(text);
    }

    private String detectFromText(String text) {
        String trimmed = text.trim();
        if (looksLikeJson(trimmed)) {
            return "tight";
        }
        if (looksLikeXml(trimmed)) {
            return "tight";
        }
        if (looksLikeCsv(trimmed)) {
            return "tight";
        }
        if (looksLikeMarkdown(trimmed)) {
            return "markdown";
        }
        if (looksLikeCode(trimmed)) {
            return "code";
        }
        if (text.length() < TIGHT_THRESHOLD) {
            return "tight";
        }
        if (text.length() < DEFAULT_THRESHOLD) {
            return "default";
        }
        return "loose";
    }

    private boolean looksLikeJson(String text) {
        return (text.startsWith("{") && text.endsWith("}")) || (text.startsWith("[") && text.endsWith("]"));
    }

    private boolean looksLikeXml(String text) {
        return text.startsWith("<") && text.endsWith(">") && text.contains("</");
    }

    private boolean looksLikeCsv(String text) {
        return text.contains(",") && text.lines().limit(CSV_SAMPLE_LINES).allMatch((it) -> it.contains(","));
    }

    private boolean looksLikeMarkdown(String text) {
        return text.contains("# ") || text.contains("```") || text.contains("* ") || text.contains("- ");
    }

    private boolean looksLikeCode(String text) {
        return text.contains("class ")
                || text.contains("def ")
                || text.contains("fun ")
                || text.contains("public ")
                || text.contains("private ")
                || (text.contains("{") && text.contains("}"));
    }
}
