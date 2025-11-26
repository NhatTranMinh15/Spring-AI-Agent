package com.agent_java.authorization_server.mapper;

import com.agent_java.authorization_server.dto.UserDto;
import com.agent_java.authorization_server.dto.UserPageDto;
import com.agent_java.authorization_server.entity.UserEntity;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;

public class UserMapper {

    public static UserDto toDto(UserEntity entity) {
        return new UserDto(
                entity.getUsername(),
                entity.isEnabled(),
                entity.getName(),
                entity.getEmail(),
                entity.getUserRoles()
                        .stream()
                        .map((t) -> t.getRole().getName())
                        .collect(Collectors.toSet())
        );
    }

    public static UserPageDto toPageDto(Page<UserEntity> page) {
        return new UserPageDto(
                page.getContent().stream().map(UserMapper::toDto).toList(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast()
        );
    }

}
