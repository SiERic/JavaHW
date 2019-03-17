package me.sieric.treeset;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class MyTreeSetTest {
    private MyTreeSet<Integer> tree;
    private MyTreeSet<Integer> emptyTree;

    @BeforeEach
    void setUp() {
        tree = new MyTreeSet<>();
        emptyTree = new MyTreeSet<>();
        for (int i = 0; i < 10; ++i) {
            tree.add((15 + i * i) % 30);
        }
        // tree = {1, 4, 6, 10, 15, 16, 19, 21, 24}
    }

    @Test
    void testAdd() {
        assertTrue(tree.add(3));
        assertFalse(tree.add(4));
    }

    @Test
    void testSize() {
        assertEquals(tree.size(), 9);
        assertFalse(tree.add(1));
        assertEquals(tree.size(), 9);
        assertTrue(tree.add(2));
        assertEquals(tree.size(), 10);

        assertEquals(emptyTree.size(), 0);
    }

    @Test
    void testContains() {
        assertTrue(tree.contains(1));
        assertFalse(tree.contains(2));
        assertThrows(ClassCastException.class, () -> tree.contains("Sasha"));
    }

    @Test
    void testRemove() {
        assertTrue(tree.remove(1));
        assertEquals(tree.size(), 8);
        assertFalse(tree.remove(1));
        assertTrue(tree.remove(24));
        assertTrue(tree.remove(10));
        assertTrue(tree.add(3));
        assertTrue(tree.remove(4));
        assertThrows(ClassCastException.class, () -> tree.remove("kek"));

        assertFalse(emptyTree.remove("kek"));
        emptyTree.add(12);
        assertTrue(emptyTree.remove(12));
    }


    @Test
    void testIterator() {
        var iterator = tree.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(iterator.next(), (Integer) 1);

        for (int i = 0; i < 8; ++i) {
            assertTrue(iterator.hasNext());
            iterator.next();
        }
        assertFalse(iterator.hasNext());
        assertThrows(NoSuchElementException.class, iterator::next);
    }

    @Test
    void testDescendingIterator() {
        var iterator = tree.descendingIterator();
        assertTrue(iterator.hasNext());
        assertEquals(iterator.next(), (Integer) 24);

        for (int i = 0; i < 8; ++i) {
            assertTrue(iterator.hasNext());
            iterator.next();
        }
        assertFalse(iterator.hasNext());
        assertThrows(NoSuchElementException.class, iterator::next);
    }

    @Test
    void testFirst() {
        assertEquals(tree.first(), (Integer) 1);
        assertThrows(NoSuchElementException.class, () -> emptyTree.first());
    }

    @Test
    void testLast() {
        assertEquals(tree.last(), (Integer) 24);
        assertThrows(NoSuchElementException.class, () -> emptyTree.last());
    }

    @Test
    void testLower() {
        assertEquals(tree.lower(4), (Integer) 1);
        assertNull(tree.lower(1));
        assertEquals(tree.lower(30), (Integer) 24);
    }

    @Test
    void testFloor() {
        assertEquals(tree.floor(3), (Integer) 1);
        assertNull(tree.floor(0));
        assertEquals(tree.floor(24), (Integer) 24);
    }

    @Test
    void testCeiling() {
        assertEquals(tree.ceiling(4), (Integer) 4);
        assertNull(tree.ceiling(30));
        assertEquals(tree.ceiling(0), (Integer) 1);
    }

    @Test
    void testHigher() {
        assertEquals(tree.higher(1), (Integer) 4);
        assertNull(tree.higher(24));
        assertEquals(tree.higher(0), (Integer) 1);
    }

    @Test
    void testClear() {
        tree.clear();
        assertEquals(tree.size(), 0);
        var iterator = tree.iterator();
        assertFalse(iterator.hasNext());
    }

    @Test
    void testTreeSetUsingComparator() {
        var cmpTree = new MyTreeSet<>(Comparator.comparingInt(a -> (int) a % 10));
        assertTrue(cmpTree.add(10));
        assertTrue(cmpTree.add(2));
        assertTrue(cmpTree.add(34));
        assertTrue(cmpTree.add(48));
        assertTrue(cmpTree.add(51));
        cmpTree.add(22);
        assertTrue(cmpTree.contains(1));
        assertEquals(cmpTree.floor(7), 34);
    }

    @Test
    void testDescendingSet() {
        var descendingTree = tree.descendingSet();
        assertTrue(descendingTree.add(3));
        assertTrue(tree.contains(3));
        assertEquals(descendingTree.last(), (Integer) 1);
        assertEquals(descendingTree.floor(8), (Integer) 10);
        var iterator = tree.iterator();
        var descendingDescendingIterator = descendingTree.descendingIterator();
        while (iterator.hasNext() && descendingDescendingIterator.hasNext()) {
            assertEquals(iterator.next(), descendingDescendingIterator.next());
        }
        assertFalse(iterator.hasNext());
        assertFalse(descendingDescendingIterator.hasNext());

    }

    @Test
    void testIteratorInvalidation() {
        var iterator = tree.iterator();
        assertTrue(tree.add(5));
        assertThrows(ConcurrentModificationException.class, iterator::hasNext);
        assertThrows(ConcurrentModificationException.class, iterator::next);
        iterator = tree.iterator();
        assertTrue(tree.remove(1));
        assertThrows(ConcurrentModificationException.class, iterator::hasNext);
        assertThrows(ConcurrentModificationException.class, iterator::next);
        assertTrue(tree.add(42));
        iterator = tree.iterator();
        tree.clear();
        assertThrows(ConcurrentModificationException.class, iterator::hasNext);
        assertThrows(ConcurrentModificationException.class, iterator::next);
    }

    @Test
    void testWithNull() {
        assertThrows(NullPointerException.class, () -> tree.add(null));
        assertThrows(NullPointerException.class, () -> tree.remove(null));
        assertThrows(NullPointerException.class, () -> tree.contains(null));

        MyTreeSet strangeTree = new MyTreeSet<Integer>((a, b) -> 0);
        assertTrue(strangeTree.add(null));
        assertTrue(strangeTree.contains(null));
        assertTrue(strangeTree.remove(null));
    }

    private static class Base implements Comparable<Base> {
        int number;
        Base(int number) {
            this.number = number;
        }

        @Override
        public int compareTo(@NotNull MyTreeSetTest.Base o) {
            return this.number - o.number;
        }
    }

    private static class Special implements Comparable<Base> {
        int number;
        Special(int number) {
            this.number = number;
        }

        @Override
        public int compareTo(@NotNull MyTreeSetTest.Base o) {
            return this.number - o.number;
        }
    }

    @Test
    void testCompareNotSuperClasses() {
        MyTreeSet<Base> kekTree = new MyTreeSet<>();
        kekTree.add(new Base(3));
        kekTree.add(new Base(20));
        assertTrue(kekTree.contains(new Special(3)));
        assertTrue(kekTree.remove(new Special(20)));
    }
}