package com.agent_java.authorization_server.config;

import com.agent_java.authorization_server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

public class SecurityConfig {

    @Autowired
    private UserRepository userRepository;

    @Bean
    @Order(2)
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.securityMatcher("/**");
        http.authorizeHttpRequests((auth) -> auth.anyRequest().authenticated());
        http.formLogin(Customizer.withDefaults());
        return http.build();
    }

    public UserDetailsService userDetailsService() {
        return (String username) -> {
            var user = userRepository.findById(username).orElseThrow(() -> new UsernameNotFoundException("User not found: $username"));
            UserDetails u = User
                    .withUsername(user.getUsername())
                    .password(user.getPassword()) // must already be encoded with BCrypt
                    .roles(user.getRoles().stream().map((t) -> t.replace("ROLE_", "")).toList().toArray(new String[user.getRoles().size()]))
                    .disabled(!user.isEnabled())
                    .build();
            return u;
        };
    }

    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
