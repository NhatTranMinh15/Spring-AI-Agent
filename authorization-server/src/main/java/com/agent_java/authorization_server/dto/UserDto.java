package com.agent_java.authorization_server.dto;

import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDto {

    String username;
    boolean enabled;
    String name;
    String email;
    Set<String> roles;

    public UserDto() {
    }

}
