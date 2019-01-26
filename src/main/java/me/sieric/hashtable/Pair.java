package me.sieric.hashtable;

/**
 * A pair to store keys and values in lists in hashtable
 */
public class Pair {

    private String key, value;

    public Pair(String key, String value) {
        this.key = key;
        this.value = value;
    }

    /**
     * Gets
     */
    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
