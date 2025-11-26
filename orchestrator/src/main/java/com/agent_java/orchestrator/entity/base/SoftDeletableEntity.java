package com.agent_java.orchestrator.entity.base;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SQLRestriction;

@MappedSuperclass
@DynamicUpdate
@SQLRestriction("deleted_at IS NULL")
@Getter
@Setter
public abstract class SoftDeletableEntity extends BaseEntity {

    @Column(name = "deleted_at")
    OffsetDateTime deletedAt = null;

    @Column(name = "active", nullable = false)
    boolean active = true;

    public void markDeleted() {
        deletedAt = OffsetDateTime.now();
        active = false;
    }
}
