package com.agent_java.orchestrator.integration.config;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

public class PostgresTestContainer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    public static final PostgreSQLContainer<?> container;

    static {
        container = new PostgreSQLContainer<>(DockerImageName.parse("pgvector/pgvector:pg17"))
                .withDatabaseName("testdb")
                .withUsername("test")
                .withPassword("test");
        container.start();
    }

    @Override
    public void initialize(ConfigurableApplicationContext context) {
        TestPropertyValues.of(
                "spring.datasource.url=" + container.getJdbcUrl(),
                "spring.datasource.username=" + container.getUsername(),
                "spring.datasource.password=" + container.getPassword()
        ).applyTo(context.getEnvironment());
    }

}
