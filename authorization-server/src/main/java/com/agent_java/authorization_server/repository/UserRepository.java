package com.agent_java.authorization_server.repository;

import com.agent_java.authorization_server.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, String>{

}
