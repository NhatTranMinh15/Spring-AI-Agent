package com.agent_java.authorization_server.service;

import com.agent_java.authorization_server.dto.UserPageDto;
import com.agent_java.authorization_server.mapper.UserMapper;
import com.agent_java.authorization_server.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    private final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Override
    public UserPageDto getUsers(int pageNumber, int pageSize) {
        var pageable = PageRequest.of(pageNumber, pageSize);
        var page = userRepository.findAll(pageable);
        logger.debug("Retrieved {} users out of {} total", page.getNumberOfElements(), page.getTotalElements());
        return UserMapper.toPageDto(page);
    }

}
