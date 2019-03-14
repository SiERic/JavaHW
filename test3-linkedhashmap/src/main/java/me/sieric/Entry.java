package me.sieric;

import java.util.Map;

public class Entry<K, V> implements Map.Entry {
    private K key;
    private V value;

    public Entry(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    @Override
    public Object setValue(Object value) {
        return null;
    }
}
