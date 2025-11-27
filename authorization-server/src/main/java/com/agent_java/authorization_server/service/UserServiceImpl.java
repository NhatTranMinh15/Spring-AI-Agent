package com.agent_java.authorization_server.service;

import com.agent_java.authorization_server.dto.CreateUserDto;
import com.agent_java.authorization_server.dto.UserPageDto;
import com.agent_java.authorization_server.entity.UserEntity;
import com.agent_java.authorization_server.entity.UserRolesEntity;
import com.agent_java.authorization_server.mapper.UserMapper;
import com.agent_java.authorization_server.repository.RolesRepository;
import com.agent_java.authorization_server.repository.UserRepository;
import com.agent_java.authorization_server.request.CreateUserRequest;
import com.agent_java.authorization_server.utils.PasswordGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RolesRepository rolesRepository;
    private final PasswordEncoder passwordEncoder;

    private final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RolesRepository rolesRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.rolesRepository = rolesRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserPageDto getUsers(int pageNumber, int pageSize) {
        var pageable = PageRequest.of(pageNumber, pageSize);
        var page = userRepository.findAll(pageable);
        logger.debug("Retrieved {} users out of {} total", page.getNumberOfElements(), page.getTotalElements());
        return UserMapper.toPageDto(page);
    }

    @Override
    @Transactional
    public CreateUserDto createUser(CreateUserRequest request) {
        // Validate username uniqueness
        if (userRepository.findUserByUserName(request.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username '" + request.getUsername() + "' already exists. Please use a different username.");
        }

        var tempPassword = PasswordGenerator.generateTempPassword();
        var encodedPassword = passwordEncoder.encode(tempPassword);

        // Map request to entity
        UserEntity userEntity = UserMapper.toUserEntity(request, encodedPassword);

        // Add roles to the user
        request.getRoles().forEach((roleName) -> {
            var role = rolesRepository.findByName(roleName)
                    .orElseThrow(() -> new IllegalArgumentException("Role '" + roleName + "' not found"));
            userEntity.getUserRoles().add(new UserRolesEntity(role, userEntity));
        });

        var savedUser = userRepository.save(userEntity);
        logger.info("User '{}' created successfully", request.getUsername());
        return UserMapper.toCreateUserDto(savedUser, tempPassword);
    }

}
