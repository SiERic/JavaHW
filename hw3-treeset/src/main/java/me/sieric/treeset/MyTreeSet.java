package me.sieric.treeset;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * Implementation of MyTreeSetInterface using binary search tree
 * @param <E> - type of the stored keys
 */
public class MyTreeSet<E> extends AbstractSet<E> implements MyTreeSetInterface<E> {

    /** Binary search tree to store elements */
    private Tree tree;
    /** Flag, indicates if order is descending*/
    private boolean isAscending = true;

    /** {@link java.util.TreeSet#TreeSet()} */
    public MyTreeSet() {
        tree = new Tree(null);
    }

    /** {@link TreeSet#TreeSet(java.util.Comparator)} */
    public MyTreeSet(Comparator<? super E> comparator) {
        tree = new Tree(comparator);
    }

    /** {@link TreeSet#descendingSet()} */
    @Override
    public MyTreeSet<E> descendingSet() {
        MyTreeSet<E> newTreeSet = new MyTreeSet<>();
        newTreeSet.tree = this.tree;
        newTreeSet.isAscending = false;
        return newTreeSet;
    }

    /** {@link TreeSet#iterator()} */
    @NotNull @Override
    public Iterator<E> iterator() {
        return (isAscending ? tree.ascendingIterator() : tree.descendingIterator());
    }

    /** {@link TreeSet#descendingIterator()} */
    @Override
    public Iterator<E> descendingIterator() {
        return (isAscending ? tree.descendingIterator() : tree.ascendingIterator());
    }

    /** {@link TreeSet#size()} */
    @Override
    public int size() {
        return tree.size();
    }

    /** {@link TreeSet#first()} */
    @Override @Nullable
    public E first() throws NoSuchElementException {
        return (isAscending ? tree.first() : tree.last());
    }

    /** {@link TreeSet#last()} */
    @Override @Nullable
    public E last() throws NoSuchElementException {
        return (isAscending ? tree.last() : tree.first());
    }

    /** {@link TreeSet#lower(java.lang.Object)} */
    @Override @Nullable
    public E lower(@Nullable E e) {
        return isAscending ? tree.lower(e) : tree.higher(e);
    }

    /** {@link TreeSet#floor(java.lang.Object)} */
    @Override @Nullable
    public E floor(@Nullable E e) {
        return isAscending ? tree.floor(e) : tree.ceiling(e);
    }

    /** {@link TreeSet#ceiling(java.lang.Object)} */
    @Override @Nullable
    public E ceiling(@Nullable E e) {
        return isAscending ? tree.ceiling(e) : tree.floor(e);
    }

    /** {@link TreeSet#higher(java.lang.Object)} */
    @Override @Nullable
    public E higher(@Nullable E e) {
        return isAscending ? tree.higher(e) : tree.lower(e);
    }

    /** {@link java.util.TreeSet#add(java.lang.Object)} */
    @Override
    public boolean add(@Nullable E e) {
        return tree.add(e);
    }

    /** {@link TreeSet#remove(java.lang.Object)} */
    @Override
    public boolean remove(@Nullable Object o) throws ClassCastException {
        return tree.remove(o);
    }

    /** {@link java.util.TreeSet#contains(java.lang.Object)} */
    @Override
    public boolean contains(@Nullable Object o) throws ClassCastException {
        return tree.contains(o);
    }

    /** {@link TreeSet#clear()} */
    @Override
    public void clear() {
        tree.clear();
    }

    /**
     * Binary Search Tree
     */
    private class Tree {

        private Node root = null;
        private int size = 0;
        private Comparator<? super E> comparator;
        private int version = 0;

        private class Node {
            Node left = null;
            Node right = null;
            Node parent;
            E key;

            Node(E key, @Nullable Node parent) {
                this.key = key;
                this.parent = parent;
            }
        }

        private Tree(Comparator<? super E> comparator) {
            this.comparator = comparator;
        }

        private int size() {
            return size;
        }

        private E first() throws NoSuchElementException {
            if (size == 0) {
                throw new NoSuchElementException();
            }
            return getKey(firstNode());
        }

        private E last() throws NoSuchElementException {
            if (size == 0) {
                throw new NoSuchElementException();
            }
            return getKey(lastNode());
        }

        @NotNull
        private Iterator<E> ascendingIterator() {
            return new AscendingIterator();
        }

        @NotNull
        private Iterator<E> descendingIterator() {
            return new DescendingIterator();
        }

        private boolean contains(@Nullable Object o) throws ClassCastException {
            if (comparator == null && o == null) {
                throw new NullPointerException();
            }
            Node node = root;
            while (node != null) {
                int cmpResult = compare(o, node.key);
                if (cmpResult == 0) {
                    return true;
                } else if (cmpResult < 0) {
                    node = node.left;
                } else {
                    node = node.right;
                }
            }
            return false;
        }

        @Nullable
        private E lower(@Nullable E e) {
            Node node = lowerBound(e);
            if (node == null) {
                return last();
            } else {
                if (node == firstNode()) {
                    return null;
                }
                return getKey(previousNode(node));
            }
        }

        @Nullable
        private E floor(@Nullable E e) {
            Node node = upperBound(e);
            if (node == null) {
                return last();
            } else {
                if (node == firstNode()) {
                    return null;
                }
                return getKey(previousNode(node));
            }
        }

        @Nullable
        private E higher(@Nullable E e) {
            Node node = upperBound(e);
            return getKey(node);
        }

        @Nullable
        private E ceiling(@Nullable E e) {
            Node node = lowerBound(e);
            return getKey(node);
        }

        private boolean add(@Nullable E e) {
            if (root == null) {
                compare(e, e);
                root = new Node(e, null);
                size++;
                version++;
                return true;
            }
            Node node = lowerBound(e);
            if (node != null && compare(e, node.key) == 0) {
                return false;
            }
            if (node == null || node.left != null) {
                if (node == null) {
                    node = root;
                } else {
                    node = node.left;
                }
                while (node.right != null) {
                    node = node.right;
                }
                node.right = new Node(e, node);
            } else {
                node.left = new Node(e, node);
            }
            version++;
            size++;
            return true;
        }

        private boolean remove(@Nullable Object o) throws ClassCastException {
            if (!contains(o)) {
                return false;
            }
            Node node = lowerBound(o);
            if (node.left == null && node.right == null) {
                if (node.parent == null) {
                    root = null;
                } else {
                    if (node.parent.left == node) {
                        node.parent.left = null;
                    } else {
                        node.parent.right = null;
                    }
                }
            }
            else if (node.right == null) {
                if (node.parent == null) {
                    root = node.left;
                }
                else if (node.parent.left == node) {
                    node.parent.left = node.left;
                    node.left.parent = node.parent;
                } else {
                    node.parent.right = node.left;
                    node.left.parent = node.parent;
                }
            } else if (node.left == null) {
                if (node.parent == null) {
                    root = node.right;
                }
                else if (node.parent.left == node) {
                    node.parent.left = node.right;
                    node.right.parent = node.parent;
                } else {
                    node.parent.right = node.right;
                    node.right.parent = node.parent;
                }

            } else {
                Node next = nextNode(node);
                swapKeys(node, next);
                if (next.parent.left == next) {
                    next.parent.left = next.right;
                } else {
                    next.parent.right = next.right;
                }
                if (next.right != null) {
                    next.right.parent = next.parent;
                }
            }
            size--;
            version++;
            return true;
        }

        private void clear() {
            root = null;
            size = 0;
            version++;
        }

        /* Even more private methods */

        @SuppressWarnings("unchecked")
        private int compare(Object o1, Object o2) {
            return comparator == null ? ((Comparable<? super E>) o1).compareTo((E) o2) : comparator.compare((E) o1, (E) o2);
        }

        private Node firstNode() {
            Node node = root;
            while (node != null && node.left != null) {
                node = node.left;
            }
            return node;
        }

        private Node lastNode() {
            Node node = root;
            while (node != null && node.right != null) {
                node = node.right;
            }
            return node;
        }

        @NotNull
        private Node nextNode(@NotNull Node node) {
            if (node.right != null) {
                node = node.right;
                while (node.left != null) {
                    node = node.left;
                }
                return node;
            }
            while (node.parent.right == node) {
                node = node.parent;
            }
            return node.parent;
        }

        @NotNull
        private Node previousNode(@NotNull Node node) {
            if (node.left != null) {
                node = node.left;
                while (node.right != null) {
                    node = node.right;
                }
                return node;
            }
            while (node.parent.left == node) {
                node = node.parent;
            }
            return node.parent;
        }

        private void swapKeys(@NotNull Node node1, @NotNull Node node2) {
            E tmp;
            tmp = node1.key;
            node1.key = node2.key;
            node2.key = tmp;
        }

        @Nullable
        private Node lowerBound(Object key) {
            Node node = root;
            Node result = null;

            while (node != null) {
                if (compare(key, node.key) <= 0) {
                    result = node;
                    node = node.left;
                } else {
                    node = node.right;
                }
            }
            return result;
        }

        @Nullable
        private Node upperBound(Object key) {
            Node node = root;
            Node result = null;

            while (node != null) {
                if (compare(key, node.key) < 0) {
                    result = node;
                    node = node.left;
                } else {
                    node = node.right;
                }
            }
            return result;
        }

        @Nullable
        private E getKey(Node node) {
            if (node == null) {
                return null;
            }
            return node.key;
        }

        private class AscendingIterator implements Iterator<E> {

            private Node current;
            private int treeVersion = version;

            @Override
            public boolean hasNext() throws ConcurrentModificationException {
                if (treeVersion != version) {
                    throw new ConcurrentModificationException();
                }
                return current != lastNode();
            }

            @Override
            public E next() throws NoSuchElementException, ConcurrentModificationException {
                if (hasNext()) {
                    if (current == null) {
                        current = firstNode();
                    } else {
                        current = nextNode(current);
                    }
                    return getKey(current);
                } else {
                    throw new NoSuchElementException();
                }
            }
        }

        private class DescendingIterator implements Iterator<E> {

            private Node current;
            int treeVersion = version;

            @Override
            public boolean hasNext() throws ConcurrentModificationException {
                if (treeVersion != version) {
                    throw new ConcurrentModificationException();
                }
                return current != firstNode();
            }

            @Override
            public E next() throws NoSuchElementException, ConcurrentModificationException {
                if (hasNext()) {
                    if (current == null) {
                        current = lastNode();
                    } else {
                        current = previousNode(current);
                    }
                    return getKey(current);
                } else {
                    throw new NoSuchElementException();
                }
            }
        }
    }
}
