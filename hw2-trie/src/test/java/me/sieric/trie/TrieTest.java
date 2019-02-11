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
    void testSerializationOfEmptyTrie() {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        assertDoesNotThrow(() -> t.serialize(buf));
        Trie newT = new Trie();
        assertDoesNotThrow(() -> newT.deserialize(new ByteArrayInputStream(buf.toByteArray())));
        assertEquals(newT.size(), 0);
    }

    @Test
    void testSerializationOfSmallTrie() {
        t.add("a");
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        assertDoesNotThrow(() -> t.serialize(buf));
        Trie newT = new Trie();
        assertDoesNotThrow(() -> newT.deserialize(new ByteArrayInputStream(buf.toByteArray())));
        assertEquals(newT.size(), 1);
        assertTrue(newT.contains("a"));
    }


    @Test
    void testSerialization() {
        fill();
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        assertDoesNotThrow(() -> t.serialize(buf));
        Trie newT = new Trie();
        assertDoesNotThrow(() -> newT.deserialize(new ByteArrayInputStream(buf.toByteArray())));
        assertTrue(newT.contains("a"));
        assertTrue(newT.contains("ab"));
        assertTrue(newT.contains("ac"));
        assertTrue(newT.contains("bebebe"));
        assertTrue(newT.contains("Sasha Top"));
        assertTrue(newT.contains("❤️"));
        assertFalse(newT.contains("kek"));
    }

    @Test
    void testDeserializeOnly() throws IOException {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        ObjectOutputStream stream = new ObjectOutputStream(buf);

        stream.writeBoolean(false);
        stream.writeInt(3);
        stream.writeInt(1);
        stream.flush();

        stream.writeChar('a');
        stream.flush();

        stream = new ObjectOutputStream(buf);
        stream.writeBoolean(true);
        stream.writeInt(3);
        stream.writeInt(2);
        stream.flush();

        stream.writeChar('b');
        stream.flush();

        stream = new ObjectOutputStream(buf);
        stream.writeBoolean(true);
        stream.writeInt(1);
        stream.writeInt(0);
        stream.flush();

        stream.writeChar('c');
        stream.flush();

        stream = new ObjectOutputStream(buf);
        stream.writeBoolean(true);
        stream.writeInt(1);
        stream.writeInt(0);
        stream.flush();


        assertDoesNotThrow(() -> t.deserialize(new ByteArrayInputStream(buf.toByteArray())));
        assertEquals(t.size(), 3);
        assertTrue(t.contains("a"));
        assertTrue(t.contains("ab"));
        assertTrue(t.contains("ac"));
    }

    @Test
    void testSerializationOnly() throws IOException {
        t.add("a");
        t.add("ab");
        t.add("ac");
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        assertDoesNotThrow(() -> t.serialize(buf));
        ByteArrayInputStream in = new ByteArrayInputStream(buf.toByteArray());
        ObjectInputStream stream2, stream3, stream4, stream = new ObjectInputStream(in);

        assertFalse(stream.readBoolean());
        assertEquals(stream.readInt(), 3);
        assertEquals(stream.readInt(), 1);

        assertEquals(stream.readChar(), 'a');

        stream2 = new ObjectInputStream(in);

        assertTrue(stream2.readBoolean());
        assertEquals(stream2.readInt(), 3);
        assertEquals(stream2.readInt(), 2);

        assertEquals(stream2.readChar(), 'b');
        stream3 = new ObjectInputStream(in);
        assertTrue(stream3.readBoolean());
        assertEquals(stream3.readInt(), 1);
        assertEquals(stream3.readInt(), 0);

        assertEquals(stream2.readChar(), 'c');

        stream4 = new ObjectInputStream(in);
        assertTrue(stream4.readBoolean());
        assertEquals(stream4.readInt(), 1);
        assertEquals(stream4.readInt(), 0);
    }
}