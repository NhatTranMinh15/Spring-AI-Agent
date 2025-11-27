package com.agent_java.authorization_server.entity;

import jakarta.persistence.*;
import java.util.UUID;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_roles")
@Data
@NoArgsConstructor
public class UserRolesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id = null;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false)
    RolesEntity role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    UserEntity user;

    public UserRolesEntity(RolesEntity role, UserEntity user) {
        this.role = role;
        this.user = user;
    }
    
    
}
