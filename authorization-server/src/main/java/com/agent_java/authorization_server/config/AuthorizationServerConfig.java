package com.agent_java.authorization_server.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import java.io.IOException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Value;

@Configuration
@EnableWebSecurity
public class AuthorizationServerConfig {

    @Value("jwt.issuer")
    private String issuer;

    @Bean
    @Order(1) // Run this filter chain first
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
        var authorizationServerConfigurer = new OAuth2AuthorizationServerConfigurer();
        var endpointsMatcher = authorizationServerConfigurer.getEndpointsMatcher();
        http.securityMatcher(endpointsMatcher)
                .authorizeHttpRequests(auth
                        -> auth.requestMatchers(
                        "/.well-known/oauth-authorization-server",
                        "/.well-known/openid-configuration"
                ).permitAll()
                        .anyRequest().authenticated()
                );
        http.csrf(csrf -> csrf.disable());
//        http.csrf(csrf -> csrf.ignoringRequestMatchers(endpointsMatcher));
        http.with(authorizationServerConfigurer, Customizer.withDefaults());
        http.exceptionHandling((exceptions) -> {
            exceptions.defaultAuthenticationEntryPointFor(new BearerTokenAuthenticationEntryPoint(), (request) -> "/userinfo".equals(request.getRequestURL().toString()));
            exceptions.defaultAuthenticationEntryPointFor(new LoginUrlAuthenticationEntryPoint("/login"), (request) -> true);
        });
        http.formLogin(Customizer.withDefaults());
        http.getConfigurer(OAuth2AuthorizationServerConfigurer.class).oidc((oidc) -> {
            oidc.clientRegistrationEndpoint(Customizer.withDefaults());
        });
        http.oauth2ResourceServer((t) -> t.jwt(Customizer.withDefaults()));
        return http.build();
    }

    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder().issuer(issuer).build();
    }

    @Bean
    public JWKSource<SecurityContext> jwkSource() throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException, UnrecoverableKeyException {
        var ks = KeyStore.getInstance("PKCS12");
        var resource = new ClassPathResource("authserver.p12");
        ks.load(resource.getInputStream(), "changeit".toCharArray());
        var key = (RSAPrivateKey) ks.getKey("authserver", "changeit".toCharArray());
        var cert = (java.security.cert.X509Certificate) ks.getCertificate("authserver");
        var publicKey = (RSAPublicKey) cert.getPublicKey();
        var rsaKey = new RSAKey.Builder(publicKey)
                .privateKey(key)
                .keyID("authserver-key") // stable key id
                .build();
        return new ImmutableJWKSet(new JWKSet(rsaKey));
    }

    @Bean
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }

    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> jwtTokenCustomizer() {
        return (JwtEncodingContext context) -> {
            Authentication principal = context.getPrincipal();
            Collection<? extends GrantedAuthority> authorities = principal.getAuthorities();
            var roles = authorities.stream().map((t) -> t.getAuthority().replace("ROLE_", "")).toList();
            if (context.getTokenType() == OAuth2TokenType.ACCESS_TOKEN) {
                context.getClaims()
                        .claim("roles", roles)
                        .claim("authorities", authorities.stream().map((t) -> t.getAuthority()).toList());
            }
            if (context.getTokenType().getValue().equals(OidcParameterNames.ID_TOKEN)) {
                context.getClaims().claim("roles", roles);
            }
        };
    }
}
