package com.aegis.autochess.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Unit JPA entity.
 */
public class UnitTest {

    @Test
    void noArgConstructorCreatesInstance() {
        Unit unit = new Unit();
        assertNotNull(unit, "No-arg constructor should create an instance");
        // defaults: id=null, strings=null, ints=0
        assertNull(unit.getId(), "id should be null on no-arg construction");
        assertNull(unit.getName(), "name should be null on no-arg construction");
        assertEquals(0, unit.getTier(), "tier should default to 0");
        assertEquals(0, unit.getCost(), "cost should default to 0");
        assertNull(unit.getTraits(), "traits should be null on no-arg construction");
    }

    @Test
    void allArgsConstructorSetsAllFields() {
        Unit unit = new Unit(10L, "Knight", 3, 7, "brave,strong");
        assertEquals(10L, unit.getId());
        assertEquals("Knight", unit.getName());
        assertEquals(3, unit.getTier());
        assertEquals(7, unit.getCost());
        assertEquals("brave,strong", unit.getTraits());
    }

    @Test
    void gettersReturnValues() {
        Unit unit = new Unit(1L, "Archer", 2, 5, "ranged");
        assertEquals(1L, unit.getId());
        assertEquals("Archer", unit.getName());
        assertEquals(2, unit.getTier());
        assertEquals(5, unit.getCost());
        assertEquals("ranged", unit.getTraits());
    }

    @Test
    void settersUpdateFields() {
        Unit unit = new Unit();
        unit.setId(2L);
        unit.setName("Mage");
        unit.setTier(4);
        unit.setCost(8);
        unit.setTraits("magic");

        assertEquals(2L, unit.getId());
        assertEquals("Mage", unit.getName());
        assertEquals(4, unit.getTier());
        assertEquals(8, unit.getCost());
        assertEquals("magic", unit.getTraits());
    }

    @Test
    void equalsReturnsTrueForSameId() {
        Unit a = new Unit(1L, "A", 1, 1, "t1");
        Unit b = new Unit(1L, "B", 2, 2, "t2");
        assertEquals(a, b, "equals should be based on id and return true for same id");
    }

    @Test
    void equalsReturnsFalseForDifferentId() {
        Unit a = new Unit(1L, "A", 1, 1, "t1");
        Unit b = new Unit(2L, "A2", 1, 1, "t1");
        assertNotEquals(a, b, "equals should return false for different ids");
    }

    @Test
    void hashCodeIsConsistentWithEquals() {
        Unit a = new Unit(3L, "X", 1, 1, "t");
        Unit b = new Unit(3L, "Y", 9, 9, "u");
        assertEquals(a.hashCode(), b.hashCode(), "equal ids must produce equal hash codes");
    }

    @Test
    void toStringContainsAllFieldValues() {
        Unit unit = new Unit(5L, "Paladin", 5, 20, "holy,shield");
        String s = unit.toString();
        assertTrue(s.contains("id=5"), "toString should include id");
        assertTrue(s.contains("name='Paladin'"), "toString should include name");
        assertTrue(s.contains("tier=5"), "toString should include tier");
        assertTrue(s.contains("cost=20"), "toString should include cost");
        assertTrue(s.contains("traits='holy,shield'"), "toString should include traits");
    }
}
