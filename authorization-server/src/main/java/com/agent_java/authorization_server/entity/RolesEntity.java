package com.agent_java.authorization_server.entity;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "roles")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RolesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id = null;

    @Column(unique = true, nullable = false)
    String name;

    @Column(length = 500)
    String description = null;

    @OneToMany(mappedBy = "role")
    Set<UserRolesEntity> userRoles = new HashSet();

    public RolesEntity(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

}
