package com.aegis.autochess.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    @Test
    void noArgConstructorDefaults() {
        Player p = new Player();
        assertNotNull(p);
        assertEquals(0, p.getGold(), "Default gold should be 0");
        assertEquals(1, p.getLevel(), "Default level should be 1");
    }

    @Test
    void allArgsConstructorSetsAllFields() {
        Player p = new Player(10L, "Alice", 50, 3);
        assertEquals(10L, p.getId());
        assertEquals("Alice", p.getName());
        assertEquals(50, p.getGold());
        assertEquals(3, p.getLevel());
    }

    @Test
    void gettersSettersWork() {
        Player p = new Player();
        p.setName("Bob");
        p.setGold(20);
        p.setLevel(2);
        assertEquals("Bob", p.getName());
        assertEquals(20, p.getGold());
        assertEquals(2, p.getLevel());
    }

    @Test
    void equalsAndHashCodeBasedOnId() {
        Player a = new Player(10L, "Alice", 0, 1);
        Player b = new Player(10L, "Alicia", 999, 9);
        // Equal ids -> equal objects
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());

        // Different ids -> not equal
        Player c = new Player(11L, "Alice", 0, 1);
        assertNotEquals(a, c);
    }

    @Test
    void toStringContainsFields() {
        Player p = new Player(10L, "Alice", 0, 1);
        String s = p.toString();
        assertTrue(s.contains("Alice"), "toString should contain name");
        assertTrue(s.contains("10"), "toString should contain id");
        assertTrue(s.contains("0"), "toString should contain gold");
        assertTrue(s.contains("1"), "toString should contain level");
    }
}
