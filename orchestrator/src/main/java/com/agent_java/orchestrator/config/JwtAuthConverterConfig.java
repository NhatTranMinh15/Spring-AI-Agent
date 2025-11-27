package com.agent_java.orchestrator.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class JwtAuthConverterConfig {

    @Autowired
    private Environment env;

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        var converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter((jwt) -> {
            List<GrantedAuthority> authorities = new ArrayList();
            List<SimpleGrantedAuthority> scpScopes;
            scpScopes = switch (jwt.getClaim("scope")) {
                case String claimString ->
                    Arrays.stream(claimString.split(" "))
                    .filter(s -> s != null && !s.isEmpty())
                    .map((t) -> new SimpleGrantedAuthority("SCOPE_" + t))
                    .toList();
                case Collection claims ->
                    claims.stream()
                    .filter((t) -> t != null)
                    .map((t) -> new SimpleGrantedAuthority("SCOPE_" + t.toString()))
                    .toList();
                default ->
                    new ArrayList();
            };
            authorities.addAll(scpScopes);

            List<SimpleGrantedAuthority> roleClaims;
            roleClaims = switch (jwt.getClaim("roles")) {
                case String rolesString ->
                    Arrays.stream(rolesString.split(","))
                    .map(t -> t.trim())
                    .filter(s -> s != null && !s.isEmpty())
                    .map((t) -> new SimpleGrantedAuthority("ROLE_" + t))
                    .toList();
                case Collection claims ->
                    claims.stream()
                    .filter((t) -> t != null)
                    .map((t) -> new SimpleGrantedAuthority("ROLE_" + t.toString()))
                    .toList();
                default ->
                    new ArrayList();
            };
            authorities.addAll(roleClaims);
            return authorities;
        });
        return converter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService prometheusUserDetailsService(PasswordEncoder passwordEncoder) {
        var username = env.getProperty("security.prometheus.username");
        var password = env.getProperty("security.prometheus.password");
        var role = env.getProperty("security.prometheus.role");
        var user = User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .roles(role)
                .build();
        return new InMemoryUserDetailsManager(user);
    }

    /**
     * Configures a security filter chain specifically for the Prometheus actuator endpoint.
     * Uses HTTP Basic authentication with an in-memory user to secure metrics scraping.
     * This filter chain has @Order(1) to ensure it's evaluated before the main OAuth2 filter chain.
     *
     * @param http                         the HttpSecurity to configure
     * @param prometheusUserDetailsService the UserDetailsService containing Prometheus credentials
     * @return the configured SecurityFilterChain for the Prometheus endpoint
     * @throws java.lang.Exception
     */
    @Bean
    @Order(1)
    public SecurityFilterChain prometheusSecurityFilterChain(
            HttpSecurity http,
            UserDetailsService prometheusUserDetailsService
    ) throws Exception {
        http
                .securityMatcher("/actuator/prometheus")
                .csrf((t) -> t.disable())
                .userDetailsService(prometheusUserDetailsService)
                .authorizeHttpRequests((t) -> t.anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults())
                .oauth2ResourceServer((t) -> t.disable());
        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            JwtAuthenticationConverter jwtAuthConverter
    ) throws Exception {
        http
                .csrf((it) -> it.disable())
                .authorizeHttpRequests((it) -> {
                    it.requestMatchers("/public/**").permitAll();
                    it.anyRequest().authenticated();
                })
                .oauth2ResourceServer((rs) -> rs.jwt((jwt) -> jwt.jwtAuthenticationConverter(jwtAuthConverter)));
        return http.build();
    }
}
