package com.agent_java.orchestrator.entity;

import com.agent_java.orchestrator.entity.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "roles")
@Data
@EqualsAndHashCode(callSuper = true)
public class RoleEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @Column(unique = true, nullable = false)
    String name;

    @Column(length = 500)
    String description;

    @ManyToMany(mappedBy = "userRoles")
    Set<UserEntity> users = new HashSet<>();

    @OneToMany(mappedBy = "role", orphanRemoval = true)
    Set<UserRoleEntity> userRoles = new HashSet();

    public RoleEntity(String name, String description) {
        this.name = name;
        this.description = description;
    }

}
