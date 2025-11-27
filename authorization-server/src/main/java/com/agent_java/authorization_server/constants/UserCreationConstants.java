package com.agent_java.authorization_server.constants;

import com.agent_java.authorization_server.enums.UserRoleEnum;
import java.util.Set;

public class UserCreationConstants {

    public static final Set<String> ALLOWED_USER_CREATION_ROLES = Set.of(UserRoleEnum.ROLE_USER.getRoleName());
}
