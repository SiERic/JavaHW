package me.sieric.hashtable;

/**
 * Represents a collection of key/value pairs
 * that are organized based on hash code of the key
 */
public class HashTable {

    /** Lists with Pairs with equal hash */

    private static final int INITIAL_CAPACITY = 2;

    private int capacity = INITIAL_CAPACITY;
    private int size = 0;

    private List[] data = new List[capacity];

    /** Gets string's hash */
    protected int getHash(String key) throws IllegalArgumentException {
        if (key == null) {
            throw new IllegalArgumentException("Key must be not null");
        }
        return Math.abs(key.hashCode()) % capacity;
    }

    /**
     * Gets number of elements stored in HashTable
     *
     * @return number of keys in HashTable
     */
    public int size() {
        return size;
    }

    /** Returns true if HashTable contains a key or false otherwise */
    public boolean contains(String key) throws IllegalArgumentException {
        int hash = getHash(key);
        return (data[hash] != null && data[hash].get(key) != null);
    }

    /**
     * Gets a value from HashTable by key
     *
     * @return value by key or null if there's no such key in HashTable
     */
    public String get(String key) throws IllegalArgumentException {
        int hash = getHash(key);
        if (data[hash] == null) {
            return null;
        }
        Pair pair = data[hash].get(key);
        if (pair != null) {
            return pair.getValue();
        }
        return null;
    }

    /**
     * Puts a value by key into HashTable
     * If hashtable previously contained the mapping for key, the old value is replaced (and returned)
     *
     * @return previous value by key or null if there's no such key in HashTable
     */
    public String put(String key, String value) throws IllegalArgumentException {
        if (value == null) {
            throw new IllegalArgumentException("Value must be not null");
        }
        int hash = getHash(key);
        if (data[hash] == null) {
            data[hash] = new List();
        }
        Pair pair = data[hash].get(key);
        if (pair != null) {
            data[hash].remove(key);
            data[hash].put(new Pair(key, value));
            return pair.getValue();
        } else {
            size++;
            if (size > capacity) {
                rebuild();
            }
            data[hash].put(new Pair(key, value));
            return null;
        }
    }

    /**
     * Removes a key with value from HashTable
     *
     * @return value by key or null if there's no such key in HashTable
     */
    public String remove(String key) throws IllegalArgumentException {
        int hash = getHash(key);
        if (data[hash] == null) {
            return null;
        }
        Pair pair = data[hash].remove(key);
        if (pair != null) {
            size--;
            return pair.getValue();
        }
        return null;

    }

    /** Clears HashTable */
    public void clear() {
        capacity = INITIAL_CAPACITY;
        size = 0;
        data = new List[capacity];
    }

    /**
     * Rebuilds hashtable, increases the capacity (x2)
     * if the number of stored elements is too big
     */
    protected void rebuild() {
        capacity *= 2;
        List[] oldData = data;
        data = new List[capacity];
        for (int i = 0; i < capacity / 2; i++) {
            if (oldData[i] == null) {
                continue;
            }
            for (Pair pair : oldData[i]) {
                int hash = getHash(pair.getKey());
                if (data[hash] == null) {
                    data[hash] = new List();
                }
                data[hash].put(pair);
            }
        }
    }
}
