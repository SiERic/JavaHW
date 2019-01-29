package me.sieric.hashtable;

/**
 * Linked list to store a pairs of key and value
 */
public class List {

    private Node head, tail;

    public List() {
        head = null;
        tail = null;
    }

    /**
     * Nodes, to iterate over List
     */
    public class Node {

        /**
         * Reference to the next Node of the List
         */
        private Node next;
        /**
         * Pair stored in the Node
         */
        private Pair data;

        private Node(Pair data) {
            this.data = data;
            next = null;
        }


        /**
         * Gets reference to the next Node
         */
        public Node getNext() {
            return next;
        }


        /**
         * Gets a pair stored in the Node
         */
        public Pair getData() {
            return data;
        }
    }

    /**
     * Gets reference to the first Node of the List (or null if list is empty)
     */
    public Node getHead() {
        return head;
    }

    /**
     * Gets reference to the last Node of the List (or null if list is empty)
     */
    public Node getTail() {
        return tail;
    }


    /**
     * Puts a new Pair into the end of the List
     */
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
        for (Node cur = head; cur != null; cur = cur.next) {
            if (cur.data.getKey().equals(key)) {
                return cur.data;
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
        Node prev = null;
        for (Node cur = head; cur != null; cur = cur.next) {
            if (cur.data.getKey().equals(key)) {
                if (prev == null) {
                    head = cur.next;
                } else {
                    prev.next = cur.next;
                }
                return cur.data;
            }
            prev = cur;
        }
        return null;
    }

    /**
     * Clears List
     */
    public void clear() {
        head = tail = null;
    }
}
