package me.sieric.trie;

import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

class TrieTest {

    private Trie t = new Trie();

    private void fill() {
        t.add("a");
        t.add("ab");
        t.add("ac");
        t.add("bebebe");
        t.add("Sasha Top");
        t.add("❤️");
    }

    @Test
    void testAddToEmptyTrie() {
        assertTrue(t.add("kek"));
    }

    @Test
    void testAddAgain() {
        t.add("kek");
        assertFalse(t.add("kek"));
    }

    @Test
    void testAdd() {
        fill();
        assertTrue(t.add("kek"));
    }

    @Test
    void testContainsInEmptyTrie() {
        assertFalse(t.contains("kek"));
    }

    @Test
    void testContains() {
        fill();
        assertTrue(t.contains("a"));
    }

    @Test
    void testContainsPrefix() {
        fill();
        assertFalse(t.contains("bebe"));
    }

    @Test
    void testRemove() {
        fill();
        assertTrue(t.remove("a"));
        assertFalse(t.contains("a"));
        assertTrue(t.contains("ab"));
    }

    @Test
    void testRemoveLongString() {
        fill();
        assertTrue(t.remove("bebebe"));
        assertFalse(t.contains("bebebe"));
    }

    @Test
    void testRemoveStringNotContainsINTrie() {
        fill();
        assertFalse(t.remove("Tortik"));
    }

    @Test
    void testSize() {
        fill();
        assertEquals(t.size(), 6);
    }

    @Test
    void testSizeOfEmptyTrie() {
        assertEquals(t.size(), 0);
    }

    @Test
    void testSizeAfterAdd() {
        fill();
        t.add("a");
        assertEquals(t.size(), 6);
    }

    @Test
    void testHowManyStartWithPrefix() {
        fill();
        assertEquals(t.howManyStartWithPrefix("a"), 3);
    }

    @Test
    void testHowManyStartWithPrefixNotInTrie() {
        fill();
        assertEquals(t.howManyStartWithPrefix("kek"), 0);
    }

    @Test
    void testHowManyStartWithEmptyPrefix() {
        fill();
        assertEquals(t.howManyStartWithPrefix(""), 6);
    }

    @Test
    void TestSerializationOfEmptyTrie() throws IOException {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        t.serialize(buf);
        Trie newT = new Trie();
        newT.deserialize(new ByteArrayInputStream(buf.toByteArray()));
        assertEquals(newT.size(), 0);
    }

    @Test
    void TestSerializationOfSmallTrie() throws IOException {
        t.add("a");
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        t.serialize(buf);
        Trie newT = new Trie();
        newT.deserialize(new ByteArrayInputStream(buf.toByteArray()));
        assertEquals(newT.size(), 1);
        assertTrue(newT.contains("a"));
    }


    @Test
    void TestSerialization() throws IOException {
        fill();
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        t.serialize(buf);
        Trie newT = new Trie();
        newT.deserialize(new ByteArrayInputStream(buf.toByteArray()));
        assertTrue(newT.contains("a"));
        assertTrue(newT.contains("ab"));
        assertTrue(newT.contains("ac"));
        assertTrue(newT.contains("bebebe"));
        assertTrue(newT.contains("Sasha Top"));
        assertTrue(newT.contains("❤️"));
        assertFalse(newT.contains("kek"));
    }
}