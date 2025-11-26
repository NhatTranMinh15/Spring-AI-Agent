package com.agent_java.authorization_server.entity;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    private String username;

    private String password;

    private boolean enabled = true;

    String name;

    String email;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    Set<UserRolesEntity> userRoles = new HashSet<>();

    public UserEntity(UUID id, String username, String password, boolean enabled, String name, String email) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.enabled = enabled;
        this.name = name;
        this.email = email;
    }

}
