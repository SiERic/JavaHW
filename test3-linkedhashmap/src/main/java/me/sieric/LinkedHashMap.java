package me.sieric;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Generic Map implementation. Returns entries in tha order they were put into.
 * @param <K> - a key type by which to store at the hash map
 * @param <V> - a value type which to store at the hash map
 */
public class LinkedHashMap<K, V> implements Map<K,V> {

    private LinkedList<K,V>[] data;
    private LinkedList<K,V> order;

    private int size = 0;
    private int capacity = INITIAL_CAPACITY;

    private static final int INITIAL_CAPACITY = 4;

    public LinkedHashMap() {
        data = (LinkedList<K, V>[])new  LinkedList[capacity];
        for (int i = 0; i < capacity; i++) {
            data[i] = new LinkedList<>();
        }
        order = new LinkedList<>();
    }

    /**
     * {@link Map#size()}
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * {@link Map#isEmpty()}
     */
    @Override
    public boolean isEmpty() {
        return (size == 0);
    }

    /**
     * {@link Map#containsKey(java.lang.Object)}
     */
    @Override
    public boolean containsKey(Object key) {
        int hash = getHash(key);
        return data[hash].contains(key);
    }

    /**
     * {@link Map#containsValue(java.lang.Object)}
     */
    @Override
    public boolean containsValue(Object value) {
        for (LinkedList<K,V>.Node node = order.getHead(); node != null; node = node.getNext()) {
            if (node.getData().getValue().equals(value)) {
                int hash = getHash(node.getData().getKey());
                return data[hash].get(node.getData().getKey()).getValue().equals(value);
            }
        }
        return false;
    }

    /**
     * {@link Map#get(java.lang.Object)}
     */
    @Override
    public V get(Object key) {
        int hash = getHash(key);
        me.sieric.Entry<K, V> entry =  data[hash].get(key);
        return entry == null ? null : entry.getValue();
    }

    /**
     * {@link Map#put(java.lang.Object, java.lang.Object)}
     */
    @Override
    public V put(K key, V value) {
        int hash = getHash(key);
        V oldValue = null;
        if (data[hash].contains(key)) {
            oldValue = data[hash].remove(key);
            size--;
        }
        data[hash].add(key, value);
        order.add(key, value);
        size++;
        if (size == capacity) {
            rebuild();
        }
        return oldValue;
    }

    /**
     * {@link Map#remove(java.lang.Object)}
     */
    @Override
    public V remove(Object key) {
        int hash = getHash(key);
        if (data[hash].contains(key)) {
            size--;
            return data[hash].remove(key);
        }
        return null;
    }

    /**
     * {@link Map#putAll(java.util.Map)}
     */
    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        for (Entry<? extends K, ? extends V> entry : m.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    /**
     * {@link Map#clear()}
     */
    @Override
    public void clear() {
        for (int i = 0; i < capacity; ++i) {
            data[i].clear();
        }
        size = 0;
        order.clear();
        capacity = INITIAL_CAPACITY;
        data = (LinkedList<K, V>[])new  LinkedList[capacity];
        for (int i = 0; i < capacity; i++) {
            data[i] = new LinkedList<>();
        }
    }

    @Override
    public Set<K> keySet() {
        return null;
    }

    @Override
    public Collection<V> values() {
        return null;
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return new Set<Entry<K, V>>() {

            @Override
            public int size() {
                return size;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public boolean contains(Object o) {
                return false;
            }

            @Override
            public Iterator<Entry<K, V>> iterator() {
                return new Iterator<Entry<K, V>>() {

                    LinkedList<K,V>.Node current = order.getHead();

                    @Override
                    public boolean hasNext() {
                        return current != null;
                    }

                    @Override
                    public Entry<K, V> next() {
                        me.sieric.Entry<K, V> entry = current.getData();
                        current = current.getNext();
                        if (!get(entry.getKey()).equals(entry.getValue())) {
                            return next();
                        }
                        return entry;
                    }
                };
            }

            @Override
            public Object[] toArray() {
                return new Object[0];
            }

            @Override
            public <T> T[] toArray(T[] a) {
                return null;
            }

            @Override
            public boolean add(Entry<K, V> kvEntry) {
                return false;
            }

            @Override
            public boolean remove(Object o) {
                return false;
            }

            @Override
            public boolean containsAll(Collection<?> c) {
                return false;
            }

            @Override
            public boolean addAll(Collection<? extends Entry<K, V>> c) {
                return false;
            }

            @Override
            public boolean retainAll(Collection<?> c) {
                return false;
            }

            @Override
            public boolean removeAll(Collection<?> c) {
                return false;
            }

            @Override
            public void clear() {

            }
        };
    }

    private void rebuild() {
        capacity *= 2;
        LinkedList<K, V>[] oldData = data;
        data = (LinkedList<K, V>[])new LinkedList[capacity];
        for (int i = 0; i < capacity; i++) {
            data[i] = new LinkedList<>();
        }

        for (LinkedList<K, V> list : oldData) {
            if (list == null) {
                continue;
            }
            for (LinkedList<K,V>.Node node = list.getHead(); node != null; node = node.getNext()) {
                me.sieric.Entry<K, V> p = node.getData();
                int hash = getHash(p.getKey());
                if (data[hash] == null) {
                    data[hash] = new LinkedList<>();
                }
                data[hash].add(p.getKey(), p.getValue());
            }
        }
    }

    private int getHash(Object s) {
        return (s.hashCode() % capacity + capacity) % capacity;
    }
}
