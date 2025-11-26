package com.agent_java.authorization_server.entity;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
public class UserEntity {

    @Id
    private String username;

    private String password;

    private boolean enabled = true;

    String name;

    String email;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "authorities",
            joinColumns = {
                @JoinColumn(name = "username")
            }
    )
    @Column(name = "authority")
    private Set<String> roles = Set.of("ROLE_USER");

    public UserEntity(String username, String password, boolean enabled, String name, String email) {
        this.username = username;
        this.password = password;
        this.enabled = enabled;
        this.name = name;
        this.email = email;
    }

}
