package com.agent_java.authorization_server.service;

import com.agent_java.authorization_server.dto.CreateUserDto;
import com.agent_java.authorization_server.dto.UserPageDto;
import com.agent_java.authorization_server.request.CreateUserRequest;

public interface UserService {

    public UserPageDto getUsers(int pageNumber, int pageSize);

    public CreateUserDto createUser(CreateUserRequest request);
}
