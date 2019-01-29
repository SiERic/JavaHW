package me.sieric.hashtable;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HashTableTest {


    private HashTable ht;

    @BeforeEach
    void setUp() {
        ht = new HashTable();
    }

    void fill() {
        String s = "a";
        for (int i = 0; i < 5; i++) {
            ht.put(s, Integer.toString(i + 1));
            s += "a";
        }
    }

    @Test
    void testSizeOfEmptyHashTable() {
        assertEquals(ht.size(), 0);
    }

    @Test
    void testSize() {
        fill();
        assertEquals(ht.size(), 5);
    }

    @Test
    void testContains() {
        fill();
        assertTrue(ht.contains("a"));
        assertFalse(ht.contains("b"));
    }

    @Test
    void testContainsInEmptyHashTable() {
        assertFalse(ht.contains("a"));
        assertFalse(ht.contains("b"));
    }


    @Test
    void testGet() {
        fill();
        assertNull(ht.get("b"));
        String s = "a";
        for (int i = 0; i < 5; i++) {
            assertEquals(ht.get(s), Integer.toString(i + 1));
            s += "a";
        }
    }

    @Test
    void testPut() {
        ht.put("a", "42");
        assertEquals(ht.get("a"), "42");
    }

    @Test
    void testGetEqualKeys() {
        ht.put("a", "1");
        assertEquals(ht.put("a", "42"), "1");
        assertEquals(ht.get("a"), "42");
    }

    @Test
    void testRemove() {
        ht.put("a", "1");
        assertEquals(ht.remove("a"), "1");
        assertFalse(ht.contains("a"));
    }

    @Test
    void testRemoveMissingKey() {
        ht.put("a", "1");
        assertNull(ht.remove("b"));
        assertTrue(ht.contains("a"));
    }

    @Test
    void testSizeAfterRemoving() {
        fill();
        ht.remove("a");
        assertEquals(ht.size(), 4);
    }

    @Test
    void testSizeAfterPuttingEqualKeys() {
        ht.put("a", "1");
        assertEquals(ht.size(), 1);
        ht.put("a", "2");
        assertEquals(ht.size(), 1);
    }

    @Test
    void testClear() {
        fill();
        ht.clear();
        assertEquals(ht.size(), 0);
        String s = "a";
        for (int i = 0; i < 5; i++) {
            assertNull(ht.get(s));
            s += "a";
        }
    }

    @Test
    void testNullKey() {
        assertThrows(IllegalArgumentException.class, () -> ht.contains(null));
        assertThrows(IllegalArgumentException.class, () -> ht.get(null));
        assertThrows(IllegalArgumentException.class, () -> ht.put(null, "kek"));
    }
}