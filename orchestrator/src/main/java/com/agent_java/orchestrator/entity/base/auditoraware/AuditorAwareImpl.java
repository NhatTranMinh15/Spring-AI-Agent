package com.agent_java.orchestrator.entity.base.auditoraware;

import com.agent_java.orchestrator.entity.User;
import com.agent_java.orchestrator.repository.UserRepository;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Profile("!test")
@Component("auditorAware")
public class AuditorAwareImpl implements AuditorAware<User> {

    @Autowired
    private UserRepository userRepository;

    @Override
    public Optional<User> getCurrentAuditor() {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String id = jwt.getClaimAsString("user_id"); // extract userId from JWT claim
        UUID uuid = UUID.fromString(id); // convert to UUID
        var user = userRepository.findById(uuid); // load User entity
        return user;
    }
}
