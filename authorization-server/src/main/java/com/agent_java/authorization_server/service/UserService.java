package com.agent_java.authorization_server.service;

import com.agent_java.authorization_server.dto.UserPageDto;

public interface UserService {

    public UserPageDto getUsers(int pageNumber, int pageSize);

}
