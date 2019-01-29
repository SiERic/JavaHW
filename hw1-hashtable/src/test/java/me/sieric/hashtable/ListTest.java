package me.sieric.hashtable;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ListTest {

    private List list;
    List empty;

    @BeforeEach
    void setUp() {
        list = new List();
        empty = new List();
    }

    void fill() {
        String s = "a";
        for (int i = 0; i < 5; i++) {
            list.put(new Pair(s, Integer.toString(i + 1)));
            s += "a";
        }
    }

    @Test
    void testGetHead() {
        fill();
        assertEquals(list.getHead().getData().getKey(), "a");
        assertEquals(list.getHead().getData().getValue(), "1");
    }

    @Test
    void testGetHeadFromEmptyList() {
        assertNull(empty.getHead());
    }

    @Test
    void testGetTail() {
        fill();
        assertEquals(list.getTail().getData().getKey(), "aaaaa");
        assertEquals(list.getTail().getData().getValue(), "5");
    }

    @Test
    void testGetTailFromEmptyList() {
        assertNull(empty.getTail());
    }

    @Test
    void testPut() {
        fill();
        list.put(new Pair("sasha", "top"));
        assertEquals(list.getTail().getData().getKey(), "sasha");
        assertEquals(list.getTail().getData().getValue(), "top");
    }

    @Test
    void testPutIntoEmptyList() {
        empty.put(new Pair("sasha", "top"));
        assertEquals(empty.getTail().getData().getKey(), "sasha");
        assertEquals(empty.getTail().getData().getValue(), "top");
    }

    @Test
    void testGet() {
        fill();
        String s = "a";
        for (int i = 0; i < 5; i++) {
            assertEquals(list.get(s).getKey(), s);
            assertEquals(list.get(s).getValue(), Integer.toString(i + 1));
            s += "a";
        }
    }

    @Test
    void testGeFromEmptyList() {
        assertNull(empty.get("sasha"));
    }

    @Test
    void testRemove() {
        fill();
        list.remove("a");
        assertNull(list.get("a"));
    }

    @Test
    void testClear() {
        fill();
        list.clear();
        assertNull(list.getHead());
        assertNull(list.getTail());
    }
}