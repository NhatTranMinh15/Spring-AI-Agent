package com.agent_java.orchestrator.entity;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;
import lombok.Data;

@Entity
@Table(name = "users")
@Data

public class User {

    @Id
    String username;
    String password;

    boolean enabled = true;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "authorities",
            joinColumns = {
                @JoinColumn(name = "username")
            }
    )
    @Column(name = "authority")
    Set<String> roles = Set.of("ROLE_USER");

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = {
                @JoinColumn(name = "username")
            },
            inverseJoinColumns = {
                @JoinColumn(name = "role_id")
            }
    )
    Set<Role> userRoles = new HashSet<>();
}
