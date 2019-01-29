package me.sieric.hashtable;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

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
    void testListIterator() {
        Iterator<Pair> it = list.iterator();
        assertFalse(it.hasNext());
        fill();
        it = list.iterator();
        assertTrue(it.hasNext());
        assertEquals(it.next().getKey(), "a");
    }

    @Test
    void testListIteratorRemove() {
        fill();
        Iterator<Pair> it = list.iterator();
        assertTrue(it.hasNext());
        it.next();
        it.remove();
        it = list.iterator();
        assertEquals(it.next().getKey(), "aa");
    }

    @Test
    void testPut() {
        fill();
        list.put(new Pair("sasha", "top"));
        assertEquals(list.get("sasha").getValue(), "top");
    }

    @Test
    void testPutIntoEmptyList() {
        empty.put(new Pair("sasha", "top"));
        assertEquals(empty.get("sasha").getValue(), "top");
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
        assertEquals(list.remove("a").getValue(), "1");
        assertNull(list.get("a"));
        assertNull(list.remove("kek"));
    }

    @Test
    void testClear() {
        fill();
        list.clear();
        Iterator<Pair> it = list.iterator();
        assertFalse(it.hasNext());
    }
}