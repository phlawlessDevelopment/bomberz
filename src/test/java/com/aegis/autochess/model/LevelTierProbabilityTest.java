package com.aegis.autochess.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for LevelTierProbability JPA entity.
 * Tests cover constructors, getters/setters, equals/hashCode, and toString.
 */
public class LevelTierProbabilityTest {

    @Test
    void noArgConstructorCreatesInstance() {
        LevelTierProbability p = new LevelTierProbability();
        assertNotNull(p, "No-arg constructor should create an instance");
    }

    @Test
    void allArgsConstructorSetsAllFields() {
        LevelTierProbability p = new LevelTierProbability(1L, 5, 2, 10);
        assertEquals(1L, p.getId());
        assertEquals(5, p.getPlayerLevel());
        assertEquals(2, p.getTier());
        assertEquals(10, p.getWeight());
    }

    @Test
    void gettersReturnCorrectValues() {
        LevelTierProbability p = new LevelTierProbability(3L, 7, 1, 4);
        assertEquals(3L, p.getId());
        assertEquals(7, p.getPlayerLevel());
        assertEquals(1, p.getTier());
        assertEquals(4, p.getWeight());
    }

    @Test
    void settersUpdateValues() {
        LevelTierProbability p = new LevelTierProbability();
        p.setId(2L);
        p.setPlayerLevel(8);
        p.setTier(3);
        p.setWeight(9);
        assertEquals(2L, p.getId());
        assertEquals(8, p.getPlayerLevel());
        assertEquals(3, p.getTier());
        assertEquals(9, p.getWeight());
    }

    @Test
    void equalsAndHashCodeBasedOnId() {
        LevelTierProbability a = new LevelTierProbability(1L, 5, 2, 3);
        LevelTierProbability b = new LevelTierProbability(1L, 6, 3, 4);
        // Same id should imply equality and same hash code
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());

        LevelTierProbability c = new LevelTierProbability(2L, 5, 2, 3);
        assertNotEquals(a, c);

        // Objects with null id should not be considered equal to those with non-null id
        LevelTierProbability d = new LevelTierProbability(null, 1, 1, 1);
        LevelTierProbability e = new LevelTierProbability(null, 1, 1, 1);
        assertNotEquals(d, a);
        // Depending on equals implementation, two null-id instances may or may not be equal.
        // We explicitly ensure they are not equal to non-null-id instances above.
    }

    @Test
    void toStringContainsFieldValues() {
        LevelTierProbability p = new LevelTierProbability(7L, 2, 1, 4);
        String s = p.toString();
        assertNotNull(s);
        // Ensure the string representation includes the actual field values
        assertTrue(s.contains("7"), "toString should include id value");
        assertTrue(s.contains("2"), "toString should include playerLevel value");
        assertTrue(s.contains("1"), "toString should include tier value");
        assertTrue(s.contains("4"), "toString should include weight value");
    }
}
