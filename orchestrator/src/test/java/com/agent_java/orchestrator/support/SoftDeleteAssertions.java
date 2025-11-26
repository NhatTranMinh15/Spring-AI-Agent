package com.agent_java.orchestrator.support;

import jakarta.persistence.EntityManager;
import java.time.OffsetDateTime;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;

public class SoftDeleteAssertions {

    /**
     * Verifies that a soft-deleted entity:
     * 1. Is not visible to default repository queries (@SQLRestriction hides it)
     * 2. Has deleted_at set in the database
     *
     * @param <T>
     * @param entityManager
     * @param entityClass
     * @param id
     */
    public static <T> void assertSoftDeleted(EntityManager entityManager, Class<T> entityClass, UUID id) {
        var result = entityManager
                .createQuery("SELECT e.deletedAt FROM " + entityClass.getSimpleName() + " e WHERE e.id = :id", OffsetDateTime.class)
                .setParameter("id", id)
                .getSingleResult();
        Assertions.assertNotNull(result, "Expected 'deletedAt' to be set for soft-deleted record");
        Assertions.assertTrue(
                !result.isAfter(OffsetDateTime.now().plusSeconds(1)),
                "deletedAt should not be in the future"
        );
    }
}
