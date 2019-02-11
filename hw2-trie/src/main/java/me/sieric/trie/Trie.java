package me.sieric.trie;

import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Implements the Trie data structure.
 * Can add new strings, remove stored,
 * check if contains, count how many stored strings starts with specified prefix.
 * Stores every string only once.
 * Can be serialized.
 */
public class Trie implements Serializable {

    /** Class to store trie nodes */
    private class Node implements Serializable {

        /** HashMap, stored links to ancestors of node */
        private HashMap<Character, Node> go;

        /** Is node terminal or not */
        private boolean isTerminal;

        /** Number of terminal nodes in subtree (including this node) */
        private int size;

        /** Constructs an empty Node */
        private Node() {
            go = new HashMap<>();
            isTerminal = false;
            size = 0;
        }

        /**
         * Goes by the edge of Trie.
         * If there's no such edge, creates a new Node
         */
        private Node go(Character symbol) {
            if (!go.containsKey(symbol)) {
                go.put(symbol, new Node());
            }
            return go.get(symbol);
        }

        /** Serialize Node into OutputStream */
        private void serialize(OutputStream out) throws IOException {
            ObjectOutputStream stream = new ObjectOutputStream(out);
            stream.writeBoolean(isTerminal);
            stream.writeInt(size);
            stream.writeInt(go.size());
            stream.flush();
            for (Map.Entry<Character, Node> entry : go.entrySet()) {
                stream.writeChar(entry.getKey());
                stream.flush();
                entry.getValue().serialize(out);
            }
            stream.flush();
        }

        /** Deserialize Node from InputStream */
        private void deserialize(InputStream in) throws IOException {
            ObjectInputStream stream = new ObjectInputStream(in);
            isTerminal = stream.readBoolean();
            size = stream.readInt();
            int goSize = stream.readInt();
            for (int i = 0; i < goSize; i++) {
                Character symbol = stream.readChar();
                Node node = new Node();
                node.deserialize(in);
                go.put(symbol, node);
            }
        }
    }

    /** The trie root */
    private Node root;

    /** Constructs an empty Trie */
    public Trie() {
        root = new Node();
    }

    /**
     * Adds a new string to Trie.
     * Returns true if string already exists, false otherwise
     */
    public boolean add(@NotNull String element) {
        return addFromNode(root, element, 0, true);
    }

    /**
     * Adds/Removes (Adds if toAdd, Removes otherwise)
     * a new string element[index:] to the subtree of given node.
     * Return true, if the operation is correct
     */
    private boolean addFromNode(Node node, String element, int index, boolean valueAtTheEndOfThePath) {
        if (element.length() == index) {
            if (node.isTerminal == valueAtTheEndOfThePath) {
                return false;
            } else {
                node.isTerminal = valueAtTheEndOfThePath;
                node.size = valueAtTheEndOfThePath? 1 : 0;
                return true;
            }
        }
        if (addFromNode(node.go(element.charAt(index)), element, index + 1, valueAtTheEndOfThePath)) {
            node.size += valueAtTheEndOfThePath ? 1 : 0;
            return true;
        } else {
            return false;
        }
    }

    /** Checks if Trie contains a string */
    public boolean contains(@NotNull String element) {
        Node currentNode = root;
        for (int i = 0; i < element.length(); i++) {
            currentNode = currentNode.go(element.charAt(i));
        }
        return currentNode.isTerminal;
    }

    /**
     * Removes string from Trie.
     * Returns true, if the string existed in the Trie before removal, false otherwise
     */
    public boolean remove(@NotNull String element) {
        return addFromNode(root, element, 0, false);
    }

    /** Gets a number of strings in Trie */
    public int size() {
        return root.size;
    }

    /** Returns number of Strings in the Trie which start with given prefix */
    public int howManyStartWithPrefix(@NotNull String prefix) {
        Node currentNode = root;
        for (int i = 0; i < prefix.length(); i++) {
            currentNode = currentNode.go(prefix.charAt(i));
        }
        return currentNode.size;
    }

    /** Serialize Trie into OutputStream */
    void serialize(OutputStream out) throws IOException {
        root.serialize(out);
    }

    /**  Deserialize Node from InputStream */
    void deserialize(InputStream in) throws IOException {
        root.deserialize(in);
    }

}
