package com.agent_java.orchestrator.component;

import org.springframework.ai.model.ApiKey;

public class SimpleApiKey implements ApiKey {

    String key;

    public SimpleApiKey(String key) {
        this.key = key;
    }

    @Override
    public String getValue() {
        return key;
    }

}
