package com.agent_java.orchestrator.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class JwtAuthConverterConfig {

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        var converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter((jwt) -> {
            List<GrantedAuthority> authorities = new ArrayList();
            List<SimpleGrantedAuthority> scpScopes;
            scpScopes = switch (jwt.getClaim("scope")) {
                case String claimString ->
                    Arrays.stream(claimString.split(" ")).filter(s -> s != null && !s.isEmpty()).map((t) -> new SimpleGrantedAuthority("SCOPE_" + t)).toList();
                case Collection claims ->
                    claims.stream().filter((t) -> t != null).map((t) -> new SimpleGrantedAuthority("SCOPE_" + t.toString())).toList();
                default ->
                    new ArrayList();
            };
            authorities.addAll(scpScopes);
            List<SimpleGrantedAuthority> roleClaims;
            roleClaims = switch (jwt.getClaim("roles")) {
                case String rolesString ->
                    Arrays.stream(rolesString.split(",")).map(t -> t.trim()).filter(s -> s != null && !s.isEmpty()).map((t) -> new SimpleGrantedAuthority("ROLE_" + t)).toList();
                case Collection claims ->
                    claims.stream().filter((t) -> t != null).map((t) -> new SimpleGrantedAuthority("ROLE_" + t.toString())).toList();
                default ->
                    new ArrayList();
            };
            authorities.addAll(roleClaims);
            return authorities;
        });
        return converter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationConverter jwtAuthConverter) throws Exception {
        http.csrf((it) -> it.disable());
        http.authorizeHttpRequests((it) -> {
            it.requestMatchers("/public/**").permitAll();
            it.requestMatchers("/api/knowledge").hasRole("ADMIN");
            it.requestMatchers("/api/roles").hasRole("ADMIN");
            it.requestMatchers(HttpMethod.GET, "/api/**")
                    .hasAnyAuthority("SCOPE_chatbot.read", "SCOPE_chatbot.write");
            List.of(HttpMethod.POST, HttpMethod.PUT, HttpMethod.DELETE).forEach(method
                    -> it.requestMatchers(method, "/api/**").hasAuthority("SCOPE_chatbot.write")
            );
            it.anyRequest().authenticated();
        });
        http.oauth2ResourceServer((rs) -> rs.jwt((jwt) -> jwt.jwtAuthenticationConverter(jwtAuthConverter)));
        return http.build();
    }
}
