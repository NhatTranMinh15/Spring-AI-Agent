package com.agent_java.authorization_server.request;

import com.agent_java.authorization_server.constants.UserCreationConstants;
import com.agent_java.authorization_server.enums.UserRoleEnum;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.Set;
import lombok.Data;

@Data
public class CreateUserRequest {

    @NotBlank(message = "Username must not be blank")
    @Size(min = 5, max = 30, message = "Username must be between 5 and 30 characters")
    String username;

    @NotBlank(message = "Name must not be blank")
    @Size(min = 1, max = 30, message = "Name must be between 1 and 30 characters")
    String name;

    @NotBlank(message = "Email must not be blank")
    @Email(message = "Invalid email format")
    String email;

    boolean sendAccountInfo;

    Set<String> roles = Set.of(UserRoleEnum.ROLE_USER.getRoleName());

    private static void checkRole(Set<String> roles) {
        boolean allow = roles.stream().allMatch(UserCreationConstants.ALLOWED_USER_CREATION_ROLES::contains);
        if (!allow) {
            String rs = String.join(", ", roles);
            String ars = String.join(", ", UserCreationConstants.ALLOWED_USER_CREATION_ROLES);
            throw new IllegalArgumentException("Only " + ars + "role are allowed when creating new users. Received: " + rs);
        }
    }

    public CreateUserRequest(String username, String name, String email, boolean sendAccountInfo) {
        this.username = username;
        this.name = name;
        this.email = email;
        this.sendAccountInfo = sendAccountInfo;
    }

    public CreateUserRequest(String username, String name, String email, boolean sendAccountInfo, Set<String> roles) {
        this(username, name, email, sendAccountInfo);
        this.roles = roles;
        checkRole(roles);
    }

}
