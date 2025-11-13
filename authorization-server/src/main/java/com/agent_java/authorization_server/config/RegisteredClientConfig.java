package com.agent_java.authorization_server.config;

import java.util.UUID;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;

@Configuration
public class RegisteredClientConfig {

    private final PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    @Bean
    public CommandLineRunner clientInitializer(RegisteredClientRepository registeredClientRepository) {
        return (String... args) -> {
            var clientId = "demo-client";
            var existing = registeredClientRepository.findByClientId(clientId);
            var correctClient = RegisteredClient.withId(existing != null ? existing.getId() : UUID.randomUUID().toString())
                    .clientId(clientId)
                    .clientSecret(passwordEncoder.encode("demo-secret"))
                    .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                    .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                    .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                    .redirectUri("http://localhost:3000/auth/callback") // Chat UI
                    .redirectUri("http://localhost:3001/auth/callback") // Admin UI
                    .scope(OidcScopes.OPENID)
                    .scope(OidcScopes.PROFILE)
                    .scope("chatbot.read")
                    .scope("chatbot.write")
                    .scope("admin.read")
                    .scope("admin.write")
                    .clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build())
                    .build();
            registeredClientRepository.save(correctClient);
            if (existing == null) {
                System.out.println("Registered new demo client with clientId=demo-client / secret=demo-secret");
            } else {
                System.out.println("Updated existing demo client with correct redirect URIs");
            }
        };
    }
}
