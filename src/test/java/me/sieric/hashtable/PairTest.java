package me.sieric.hashtable;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PairTest {

    @Test
    void testGetKey() {
        Pair p = new Pair("Sanya", "top");
        assertEquals("Sanya", p.getKey());
    }

    @Test
    void TestGetValue() {
        Pair p = new Pair("Sanya", "top");
        assertEquals("top", p.getValue());
    }
}