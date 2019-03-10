package me.sieric.hashtable;

import java.util.Iterator;

/** Linked list to store a pairs of key and value */
public class List implements Iterable<Pair> {

    private Node head = null;
    private Node tail = null;

    /** Get the List Iterator */
    public Iterator<Pair> iterator() {
        return new ListIterator();
    }

    /** Puts a new Pair into the end of the List */
    public void put(Pair data) {
        if (head == null) {
            head = new Node(data);
            tail = head;
        } else {
            tail.next = new Node(data);
            tail = tail.next;
        }
    }

    /**
     * Gets a Pair from List by key
     * @return the Pair with given key or null if there's no such Pair
     */
    public Pair get(String key) {
        ListIterator it = new ListIterator();
        while (it.hasNext()) {
            Pair data = it.next();
            if (data.getKey().equals(key)) {
                return data;
            }
        }
        return null;
    }

    /**
     * Removes a Pair from List by key
     * @return the Pair with given key or null if there's no such Pair
     */
    public Pair remove(String key) {
        ListIterator it = new ListIterator();
        while (it.hasNext()) {
            Pair data = it.next();
            if (data.getKey().equals(key)) {
                it.remove();
                return data;
            }
        }
        return null;
    }

    /** Clears List */
    public void clear() {
        head = null;
        tail = null;
    }

    /** Nodes, to store pairs in the List */
    private class Node {

        /** Reference to the next Node of the List */
        private Node next = null;

        /** Pair stored in the Node */
        private Pair data;

        private Node(Pair data) {
            this.data = data;
        }
    }

    /**
     * Iterator to iterate through the List.
     * All of the iterators are invalidated after any remove/put operation on the List
     */
    private class ListIterator implements Iterator<Pair> {

        private Node current;
        private Node previous = null;
        private Node previousOfPrevious = null;

        public ListIterator() {
            current = List.this.head;
        }

        /** Checks if whether there is an element to advance */
        public boolean hasNext() {
            return current != null;
        }

        /**
         * Advance one position further
         * @return Pair in position before advance
         */
        public Pair next() {
            Pair data = current.data;
            previousOfPrevious = previous;
            previous = current;
            current = current.next;
            return data;
        }

        /** Remove the previous element */
        public void remove() {
            if (previous == null) {
                throw new IllegalStateException();
            } else if (previousOfPrevious == null) {
                List.this.head = current;
            } else {
                previousOfPrevious.next = current;
            }
        }
    }
}
