package me.sieric.hashtable;

import java.util.Iterator;

/** Linked list to store a pairs of key and value */
public class List implements Iterable<Pair> {

    private Node head;
    private Node tail;

    public List() {
        head = null;
        tail = null;
    }

    /** Nodes, to store pairs in the List */
    private class Node {

        /** Reference to the next Node of the List */
        private Node next;

        /** Pair stored in the Node */
        private Pair data;

        private Node(Pair data) {
            this.data = data;
            next = null;
        }
    }


    /**
     * Iterator to iterate through the List.
     * All of the iterators are invalidated after any remove/put operation on the List
     */
    private class ListIterator implements Iterator<Pair> {

        private Node cur;
        private Node prev = null;
        private Node prevOfPrev = null;

        public ListIterator() {
            cur = List.this.head;
        }

        /** Checks if whether there is an element to advance */
        public boolean hasNext() {
            return cur != null;
        }

        /**
         * Advance one position further
         *
         * @return Pair in position before advance
         */
        public Pair next() {
            Pair data = cur.data;
            prevOfPrev = prev;
            prev = cur;
            cur = cur.next;
            return data;
        }

        /** Remove the previous element */
        public void remove() {
            if (prev == null) {
                throw new IllegalStateException();
            } else if (prevOfPrev == null) {
                List.this.head = cur;
            } else {
                prevOfPrev.next = cur;
            }
        }
    }

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
     *
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
     *
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
}
