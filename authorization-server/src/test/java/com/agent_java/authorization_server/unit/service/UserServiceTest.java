package com.agent_java.authorization_server.unit.service;

import com.agent_java.authorization_server.entity.UserEntity;
import com.agent_java.authorization_server.repository.UserRepository;
import com.agent_java.authorization_server.service.UserServiceImpl;
import java.util.List;
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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

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
}
