package com.agent_java.orchestrator.utils;

public class Utils {

    public static String getShortToolName(String originalToolName) {
        if (originalToolName == null || originalToolName.isBlank()) {
            return null;
        }
        String shortName = originalToolName.substring(originalToolName.lastIndexOf("_") + 1);
        return shortName.isBlank() ? null : shortName;
    }

}
