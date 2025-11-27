package com.agent_java.authorization_server.entity;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    private String username;

    private String password;

    private boolean enabled = true;

    String name;

    @Column(unique = true, nullable = false)
    String email;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = {CascadeType.ALL}, orphanRemoval = true)
    Set<UserRolesEntity> userRoles = new HashSet<>();

    public UserEntity(UUID id, String username, String password, boolean enabled, String name, String email) {
        this(username, password, enabled, name, email);
        this.id = id;
    }

    public UserEntity(String username, String password, boolean enabled, String name, String email) {
        this.username = username;
        this.password = password;
        this.enabled = enabled;
        this.name = name;
        this.email = email;
    }

}
