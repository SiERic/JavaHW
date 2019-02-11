package me.sieric.treeset;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * Implementation of MyTreeSetInterface using binary search tree
 * @param <E> - type of the stored keys
 */
public class MyTreeSet<E> extends AbstractSet<E> implements MyTreeSetInterface<E> {

    /** Binary search tree to store elements */
    private Tree<E> tree;
    /** Flag, indicates if order is descending*/
    private boolean isAscending = true;

    /**
     * Constructs a new, empty tree set, sorted according to the natural ordering of its elements.
     * All elements inserted into the set must implement the Comparable interface.
     * Furthermore, all such elements must be mutually comparable: e1.compareTo(e2) must not throw a ClassCastException for any elements e1 and e2 in the set.
     * If the user attempts to add an element to the set that violates this constraint
     * (for example, the user attempts to add a string element to a set whose elements are integers), the add call will throw a ClassCastException
     */
    @SuppressWarnings("unchecked")
    public MyTreeSet() {
        tree = new Tree<>((a, other) -> ((Comparable) a).compareTo(other));
    }

    /**
     * Constructs a new, empty tree set, sorted according to the specified comparator.
     * All elements inserted into the set must be mutually comparable by the specified comparator:
     * comparator.compare(e1, e2) must not throw a ClassCastException for any elements e1 and e2 in the set.
     * If the user attempts to add an element to the set that violates this constraint, the add call will throw a ClassCastException
     * @param comparator - the comparator that will be used to order this set. If null, the natural ordering of the elements will be used
     */
    public MyTreeSet(Comparator<? super E> comparator) {
        tree = new Tree<>(comparator);
    }

    /**
     * Returns a reverse order view of the elements contained in this set.
     * The descending set is backed by this set, so changes to the set are reflected in the descending set, and vice-versa.
     * If either set is modified while an iteration over either set is in progress (except through the iterator's own remove operation),
     * the results of the iteration are undefined.
     * @return a reverse order view of this set
     */
    @Override
    public MyTreeSet<E> descendingSet() {
        MyTreeSet<E> newts = new MyTreeSet<>();
        newts.tree = this.tree;
        newts.isAscending = false;
        return newts;
    }

    /**
     * Returns an iterator over the elements in this set in ascending order.
     * The functions add and remove invalidate iterators.
     * @return an iterator over the elements in this set in ascending order
     */
    @NotNull
    @Override
    public Iterator<E> iterator() {
        if (isAscending) {
            return tree.ascendingIterator();
        } else {
            return tree.descendingIterator();
        }
    }

    /**
     * Returns an iterator over the elements in this set in descending order.
     * The functions add and remove invalidate iterators.
     * @return an iterator over the elements in this set in descending order
     */
    @Override
    public Iterator<E> descendingIterator() {
        if (isAscending) {
            return tree.descendingIterator();
        } else {
            return tree.ascendingIterator();
        }
    }

    /**
     * Returns the number of elements in this set (its cardinality)
     * @return the number of elements in this set (its cardinality)
     */
    @Override
    public int size() {
        return tree.size();
    }

    /**
     * Returns the first (lowest) element currently in this set
     * @return the first (lowest) element currently in this set
     */
    @Override
    @Nullable
    public E first() {
        if (isAscending) {
            return tree.first();
        } else {
            return tree.last();
        }
    }

    /**
     * Returns the last (highest) element currently in this set
     * @return the last (highest) element currently in this set
     */
    @Override
    @Nullable
    public E last() {
        if (isAscending) {
            return tree.last();
        } else {
            return tree.first();
        }
    }

    /**
     * Returns the greatest element in this set strictly less than the given element, or null if there is no such element
     * @param e - the value to match
     * @return the greatest element less than e, or null if there is no such element
     */
    @Override @Nullable
    public E lower(E e) {
        if (isAscending) {
            return tree.lower(e);
        } else {
            return tree.higher(e);
        }
    }

    /**
     * Returns the greatest element in this set less than or equal to the given element, or null if there is no such element
     * @param e - the value to match
     * @return the greatest element less than or equal to e, or null if there is no such element
     */
    @Override @Nullable
    public E floor(E e) {
        if (isAscending) {
            return tree.floor(e);
        } else {
            return tree.ceiling(e);
        }
    }

    /**
     * Returns the least element in this set greater than or equal to the given element, or null if there is no such element
     * @param e - the value to match
     * @return the least element greater than or equal to e, or null if there is no such element
     */
    @Override @Nullable
    public E ceiling(E e) {
        if (isAscending) {
            return tree.ceiling(e);
        } else {
            return tree.floor(e);
        }
    }

    /**
     * Returns the least element in this set strictly greater than the given element, or null if there is no such element
     * @param e - the value to match
     * @return the least element greater than e, or null if there is no such element
     */
    @Override @Nullable
    public E higher(E e) {
        if (isAscending) {
            return tree.higher(e);
        } else {
            return tree.lower(e);
        }
    }

    /**
     * Adds the specified element to this set if it is not already present.
     * More formally, adds the specified element e to this set if the set contains no element e2 such that (e==null ? e2==null : e.equals(e2)).
     * If this set already contains the element, the call leaves the set unchanged and returns false
     * @param e - element to be added to this set
     * @return true if this set did not already contain the specified element
     */
    @Override
    public boolean add(E e) {
        return tree.add(e);
    }

    /**
     * Removes the specified element from this set if it is present.
     * More formally, removes an element e such that (o==null ? e==null : o.equals(e)), if this set contains such an element.
     * Returns true if this set contained the element (or equivalently, if this set changed as a result of the call).
     * (This set will not contain the element once the call returns.)
     * @param o - object to be removed from this set, if present
     * @return true if this set contained the specified element
     * @throws ClassCastException if can't cast o to E
     */
    @Override
    public boolean remove(Object o) throws ClassCastException {
        return tree.remove(o);
    }


    /**
     /**
     * Returns true if this set contains the specified element.     *
     * @param o - element whose presence in this set is to be tested
     * @return {true} if this set contains the specified element
     * @throws ClassCastException if can't cast o to E
     */
    @Override
    public boolean contains(Object o) throws ClassCastException {
        return tree.contains(o);
    }

    /** Removes all of the elements from this set. The set will be empty after this call returns */
    @Override
    public void clear() {
        tree.clear();
    }

    /**
     * Binary Search Tree
     * @param <E> - type of stored keys
     */
    private static class Tree<E> {

        private Node root = null;
        private int size = 0;
        private Comparator<? super E> comparator;
        private int version = 0;

        private class Node {
            Node left = null;
            Node right = null;
            Node parent;
            E key;

            Node(E key, Node parent) {
                this.key = key;
                this.parent = parent;
            }
        }

        private Tree(Comparator<? super E> comparator) {
            this.comparator = comparator;
        }

        @Nullable
        private Node lowerBound(@NotNull E key) {
            Node node = root;
            Node result = null;

            while (node != null) {
                if (comparator.compare(key, node.key) <= 0) {
                    result = node;
                    node = node.left;
                } else {
                    node = node.right;
                }
            }
            return result;
        }

        @Nullable
        private Node upperBound(@NotNull E key) {
            Node node = root;
            Node result = null;

            while (node != null) {
                if (comparator.compare(key, node.key) < 0) {
                    result = node;
                    node = node.left;
                } else {
                    node = node.right;
                }
            }
            return result;
        }

        @Contract(value = "null -> null", pure = true)
        @Nullable
        private E getKey(Node node) {
            if (node == null) {
                return null;
            }
            return node.key;
        }

        @Nullable
        private Node firstNode() {
            Node node = root;
            while (node != null && node.left != null) {
                node = node.left;
            }
            return node;
        }

        @Nullable
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

        private int size() {
            return size;
        }

        @Nullable
        private E first() {
            return getKey(firstNode());
        }

        @Nullable
        private E last() {
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
        @SuppressWarnings("unchecked")
        private boolean contains(Object o) throws ClassCastException {
            Node node = root;
            while (node != null) {
                int cmpResult = comparator.compare(node.key, (E) o);
                if (cmpResult == 0) {
                    return true;
                } else if (cmpResult > 0) {
                    node = node.left;
                } else {
                    node = node.right;
                }
            }
            return false;
        }


        @Nullable
        private E lower(@NotNull E e) {
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
        private E floor(@NotNull E e) {
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
        private E higher(@NotNull E e) {
            Node node = upperBound(e);
            return getKey(node);
        }

        @Nullable
        private E ceiling(@NotNull E e) {
            Node node = lowerBound(e);
            return getKey(node);
        }

        private boolean add(@NotNull E e) {
            if (root == null) {
                root = new Node(e, null);
                size++;
                version++;
                return true;
            }
            Node node = lowerBound(e);
            if (node != null && comparator.compare(getKey(node), e) == 0) {
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

        @SuppressWarnings("unchecked")
        private boolean remove(@NotNull Object o) throws ClassCastException {
            if (!contains(o)) {
                return false;
            }
            Node node = lowerBound((E) o);
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
        }
    }

}
