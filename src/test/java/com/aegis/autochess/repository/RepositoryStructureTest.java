package com.aegis.autochess.repository;

import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for repository interface structure.
 * Verifies inheritance from JpaRepository and presence of required query methods.
 */
public class RepositoryStructureTest {

    @Test
    void unitRepositoryExtendsJpaRepositoryAndDeclaresFindByTier() {
        Class<UnitRepository> cls = UnitRepository.class;
        assertTrue(JpaRepository.class.isAssignableFrom(cls),
                "UnitRepository should extend JpaRepository");
        boolean hasFindByTier = Arrays.stream(cls.getDeclaredMethods())
                .anyMatch(m -> m.getName().equals("findByTier"));
        assertTrue(hasFindByTier, "UnitRepository should declare a findByTier method");
    }

    @Test
    void levelTierProbabilityRepositoryExtendsJpaRepositoryAndDeclaresFindByPlayerLevel() {
        Class<LevelTierProbabilityRepository> cls = LevelTierProbabilityRepository.class;
        assertTrue(JpaRepository.class.isAssignableFrom(cls),
                "LevelTierProbabilityRepository should extend JpaRepository");
        boolean hasFindByPlayerLevel = Arrays.stream(cls.getDeclaredMethods())
                .anyMatch(m -> m.getName().equals("findByPlayerLevel"));
        assertTrue(hasFindByPlayerLevel,
                "LevelTierProbabilityRepository should declare a findByPlayerLevel method");
    }

    @Test
    void playerRepositoryExtendsJpaRepository() {
        Class<PlayerRepository> cls = PlayerRepository.class;
        assertTrue(JpaRepository.class.isAssignableFrom(cls),
                "PlayerRepository should extend JpaRepository");
    }
}
