package com.agent_java.authorization_server.dto;

import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserDto {

    String username;

    String name;

    String email;

    Set<String> roles;

    String temporaryPassword;
}
