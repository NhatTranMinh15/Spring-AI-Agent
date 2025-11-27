package com.agent_java.authorization_server.repository;

import com.agent_java.authorization_server.entity.UserEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<UserEntity, String>, JpaSpecificationExecutor<UserEntity> {

    @Query(value = "SELECT u "
            + "FROM UserEntity u "
            + "LEFT JOIN FETCH u.userRoles ur "
            + "LEFT JOIN FETCH ur.role "
            + "WHERE u.username = :username"
    )
    public Optional<UserEntity> findUserByUserName(@Param("username") String username);

    public Optional<UserEntity> findByEmail(String email);
}
