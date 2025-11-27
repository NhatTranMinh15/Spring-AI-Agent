package com.agent_java.authorization_server.unit.service;

import com.agent_java.authorization_server.entity.RolesEntity;
import com.agent_java.authorization_server.entity.UserEntity;
import com.agent_java.authorization_server.entity.UserRolesEntity;
import com.agent_java.authorization_server.enums.UserRoleEnum;
import com.agent_java.authorization_server.repository.RolesRepository;
import com.agent_java.authorization_server.repository.UserRepository;
import com.agent_java.authorization_server.request.CreateUserRequest;
import com.agent_java.authorization_server.service.UserServiceImpl;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.any;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RolesRepository rolesRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    public void getUsers_return_paginated_users() {
        var users = List.of(
                new UserEntity(UUID.randomUUID(), "testuser1", "password1", true, "Test", "testuser@gmail.com"),
                new UserEntity(UUID.randomUUID(), "admin", "adminpass", true, "Admin", "admin@gmail.com")
        );
        var pageable = PageRequest.of(0, 2);
        Page<UserEntity> page = new PageImpl(users, pageable, users.size());

        when(userRepository.findAll(pageable)).thenReturn(page);

        var result = userService.getUsers(0, 2);
        assertEquals(1, result.getTotalPages());
        assertEquals(2, result.getUsers().size());
        verify(userRepository, times(1)).findAll(pageable);
    }

    @Test
    public void createUser_should_create_user_with_temporary_password_and_ROLE_USER() {
        var request = new CreateUserRequest("newuser", "New User", "newuser@gmail.com", true);

        var encodedPassword = "encodedTempPassword123";
        var userId = UUID.randomUUID();
        var roleEntity = new RolesEntity(UUID.randomUUID(), UserRoleEnum.ROLE_USER.getRoleName());

        var userEntity = new UserEntity(userId, request.getUsername(), encodedPassword, true, request.getName(), request.getEmail()); // Add role to user entity

        userEntity.getUserRoles().add(new UserRolesEntity(roleEntity, userEntity));

        when(userRepository.findUserByUserName(request.getUsername())).thenReturn(Optional.empty());

        when(passwordEncoder.encode(any())).thenReturn(encodedPassword);

        when(rolesRepository.findByName(UserRoleEnum.ROLE_USER.getRoleName())).thenReturn(Optional.of(roleEntity));

        when(userRepository.save(any())).thenReturn(userEntity);

        var result = userService.createUser(request);

        assertEquals("newuser", result.getUsername());
        assertEquals("New User", result.getName());
        assertEquals("newuser@gmail.com", result.getEmail());
        assertEquals(Set.of(UserRoleEnum.ROLE_USER.getRoleName()), result.getRoles());
//        assertTrue(!result.getTemporaryPassword().isBlank());
        assertFalse(result.getTemporaryPassword().isBlank());
        verify(userRepository, times(1)).save(any());
        verify(passwordEncoder, times(1)).encode(any());
        verify(rolesRepository, times(1)).findByName(UserRoleEnum.ROLE_USER.getRoleName());
    }

    @Test
    public void createUser_should_use_default_ROLE_USER_when_no_role_specified() {
        var request = new CreateUserRequest("defaultroleuser", "Default Role User", "defaultrole@gmail.com", true);

        var encodedPassword = "encodedTempPassword123";
        var userId = UUID.randomUUID();
        var roleEntity = new RolesEntity(UUID.randomUUID(), UserRoleEnum.ROLE_USER.getRoleName());

        var userEntity = new UserEntity(userId, request.getUsername(), encodedPassword, true, request.getName(), request.getEmail());

        // Add role to user entity
        userEntity.getUserRoles().add(new UserRolesEntity(roleEntity, userEntity));

        when(userRepository.findUserByUserName(request.getUsername())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(any())).thenReturn(encodedPassword);
        when(rolesRepository.findByName(UserRoleEnum.ROLE_USER.getRoleName())).thenReturn(Optional.of(roleEntity));
        when(userRepository.save(any())).thenReturn(userEntity);

        var result = userService.createUser(request);

        assertEquals("defaultroleuser", result.getUsername());
        assertEquals(Set.of(UserRoleEnum.ROLE_USER.getRoleName()), result.getRoles());
        verify(userRepository, times(1)).save(any());
        verify(rolesRepository, times(1)).findByName(UserRoleEnum.ROLE_USER.getRoleName());
    }
}
