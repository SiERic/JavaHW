package me.sieric;

/**
 * A singly linked list to be used into HashMap,
 * which stores a pair of key and value.
 */
public class LinkedList<K, V> {

    private Node head;
    private Node tail;

    /**
     * A list consists of nodes.
     * Node can be used to iterate over List
     */
    public class Node {
        private Node next;
        private me.sieric.Entry<K, V> data;

        private Node(Entry<K, V> data) {
            this.data = data;
        }

        public Node getNext() {
            return next;
        }

        public Entry<K, V> getData() {
            return data;
        }
    }

    public Node getHead() {
        return head;
    }

    /**
     * Adds a new pair of key and value into the end of list
     */
    public void add(K key, V value) {
        if (head == null) {
            head = new Node(new me.sieric.Entry<>(key, value));
            tail = head;
            return;
        }
        tail.next = new Node(new me.sieric.Entry<>(key, value));
        tail = tail.next;
    }

    /**
     * Gets a pair from list by key
     */
    public me.sieric.Entry<K, V> get(Object key) {
        for (Node current = head; current != null; current = current.next) {
            if (current.data.getKey().equals(key)) {
                return current.data;
            }
        }
        return null;
    }

    /**
     * Remove a pair from list by key
     */
    public V remove(Object key) {
        if (head.data.getKey().equals(key)) {
            me.sieric.Entry removed = head.data;
            head = head.next;
            return (V) removed.getValue();
        }
        for (Node current = head; current.next != null; current = current.next) {
            if (current.next.data.getKey().equals(key)) {
                me.sieric.Entry removed = current.next.data;
                current.next = current.next.next;
                return (V) removed.getValue();
            }
        }
        return null;
    }

    /**
     * Checks if there is a pair with such key.
     */
    public boolean contains(Object key) {
        for (Node current = head; current != null; current = current.next) {
            if (current.data.getKey().equals(key)) {
                return true;
            }
        }
        return false;
    }

    /** Clears the list */
    public void clear() {
        head = null;
        tail = null;
    }
}