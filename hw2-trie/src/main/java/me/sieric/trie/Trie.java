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

    /** The trie root */
    private Node root = new Node();

    /**
     * Adds a new string to Trie.
     * Returns true if string already exists, false otherwise
     * @return true if string already exists, false otherwise
     */
    public boolean add(@NotNull String element) {
        return addFromNode(root, element, 0, true);
    }

    /**
     * Adds/Removes (Adds if toAdd, Removes otherwise)
     * a new string element[index:] to the subtree of given node.
     * Return true, if the operation is correct
     * @param node - a current node, where the path starts
     * @param element - a string, which suffix to add/remove from trie
     * @param index - a position from which the suffix starts
     * @param valueAtTheEndOfThePath - a flag, which is true if suffix is adding to Trie and false if deleting
     * @return true if a new string has been added/removed or false otherwise
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

    /**
     * Checks if Trie contains a string
     * @param element - a string for which to check whether it is in Trie
     * @return true if the given string is in the Trie, false otherwise
     */
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
     * @param element - a string to remove
     * @return true, if the string existed in the Trie before removal, false otherwise
     */
    public boolean remove(@NotNull String element) {
        return addFromNode(root, element, 0, false);
    }

    /**
     * Gets a number of strings in Trie
     * @return number of strings in Trie
     */
    public int size() {
        return root.size;
    }

    /**
     * Returns number of strings in the Trie which start with given prefix
     * @return  number of strings in the Trie which start with given prefix
     */
    public int howManyStartWithPrefix(@NotNull String prefix) {
        Node currentNode = root;
        for (int i = 0; i < prefix.length(); i++) {
            currentNode = currentNode.go(prefix.charAt(i));
        }
        return currentNode.size;
    }

    /**
     * Serialize Trie into OutputStream
     * @param out - OutputStream to write into
     * @throws IOException if writing into stream failed
     */
    void serialize(OutputStream out) throws IOException {
        root.serialize(out);
    }

    /**
     * Deserialize Node from InputStream
     * @param in - InputStream to read from
     * @throws IOException if reading from stream failed
     */
    void deserialize(InputStream in) throws IOException {
        root.deserialize(in);
    }

    /** Class to store trie nodes */
    private class Node implements Serializable {

        /** HashMap, stored links to ancestors of node */
        private HashMap<Character, Node> go = new HashMap<>();

        /** Is node terminal or not */
        private boolean isTerminal = false;

        /** Number of terminal nodes in subtree (including this node) */
        private int size = 0;

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

        /**
         * Serialize Node into OutputStream
         * @param out - OutputStream to write into
         * @throws IOException if writing into stream failed
         */
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
        }

        /**
         * Deserialize Node from InputStream
         * @param in - InputStream to read from
         * @throws IOException if reading from stream failed
         */
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

}
