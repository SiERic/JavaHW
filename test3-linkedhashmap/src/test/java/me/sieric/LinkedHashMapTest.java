package me.sieric;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LinkedHashMapTest {


    private me.sieric.LinkedHashMap<Integer, Integer> hashMap;
    private me.sieric.LinkedHashMap<Integer, Integer> emptyHashMap;

    @BeforeEach
    void setUp() {
        emptyHashMap = new me.sieric.LinkedHashMap<>();
        hashMap = new me.sieric.LinkedHashMap<>();
        for (int i = 0; i < 10; i++) {
            hashMap.put(i * 10000, i);
        }
    }

    @Test
    void testContains() {
        assertTrue(hashMap.containsKey(10000));
        assertFalse(hashMap.containsKey(90));
        assertFalse(emptyHashMap.containsKey(8));
        assertFalse(emptyHashMap.containsValue(8));
        assertTrue(hashMap.containsValue(9));
    }

    @Test
    void testEmpty() {
        assertTrue(emptyHashMap.isEmpty());
        assertFalse(hashMap.isEmpty());
    }

    @Test
    void testSize() {
        assertEquals(0, emptyHashMap.size());
        assertEquals(10, hashMap.size());
    }

    @Test
    void testSizeSameKeys() {
        emptyHashMap.put(1, 2);
        emptyHashMap.put(1, 3);
        assertEquals(1, emptyHashMap.size());
    }

    @Test
    void testSizeAfterRemove() {
        emptyHashMap.put(1, 1);
        emptyHashMap.put(2, 2);
        assertEquals((Integer)1, emptyHashMap.remove(1));
        assertEquals(1, emptyHashMap.size());
    }

    @Test
    void testGet() {
        assertNull(emptyHashMap.get(1));
        for (int i = 0; i < 10; i++) {
            assertEquals((Integer)i, hashMap.get(i * 10000));
        }
        assertNull(hashMap.get(2));
        hashMap.put(10000, 2);
        assertEquals((Integer)2, hashMap.get(10000));
    }

    @Test
    void testPut() {
        assertEquals((Integer)1, hashMap.put(10000, 2));
    }

    @Test
    void testRemove() {
        assertNull(emptyHashMap.remove(2));
        assertEquals((Integer)1, hashMap.remove(10000));
        assertNull(hashMap.remove(2));
    }

    @Test
    void testClear() {
        hashMap.clear();
        assertTrue(hashMap.isEmpty());
    }

    @Test
    void testEntrySet() {
        int i = 0;
        for (var entry : hashMap.entrySet()) {
            assertEquals((Integer)(i * 10000), entry.getKey());
            assertEquals((Integer)(i), entry.getValue());
            i++;
        }
    }
}