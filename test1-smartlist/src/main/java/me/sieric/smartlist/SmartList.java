package me.sieric.smartlist;

import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Implementation of List
 * @param <E> - type of stored elements
 */
public class SmartList<E> implements List<E> {

    public int size = 0;
    public Object data = null;

    public SmartList() {}
    public SmartList(Collection<? extends E> c) {
        for (E e : c) {
            add(e);
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return (size == 0);
    }

    @Override
    public boolean contains(Object o) {
        for (E e : this) {
            if (e.equals(o)) {
                return true;
            }
        }
        return false;
    }

    private class NoneElementIterator implements ListIterator<E>  {

        @Override
        public boolean hasNext() {
            return false;
        }

        @Override
        public E next() throws NoSuchElementException {
            throw new NoSuchElementException();
        }

        @Override
        public boolean hasPrevious() {
            return false;
        }

        @Override
        public E previous() throws NoSuchElementException {
            throw new NoSuchElementException();
        }

        @Override
        public int nextIndex() {
            return 0;
        }

        @Override
        public int previousIndex() {
            return 0;
        }

        @Override
        public void remove() {

        }

        @Override
        public void set(E e) throws NoSuchElementException {
            throw new NoSuchElementException();
        }

        @Override
        public void add(E e) throws NoSuchElementException {
            throw new NoSuchElementException();
        }
    }

    private class OneElementIterator implements ListIterator<E>  {

        boolean hasNext = true;
        @Override
        public boolean hasNext() {
            return hasNext;
        }

        @Override
        public E next() throws NoSuchElementException {
            if (!hasNext) {
                throw new NoSuchElementException();
            }
            hasNext = false;
            return (E)data;
        }

        @Override
        public boolean hasPrevious() {
            return !hasNext;
        }

        @Override
        public E previous() {
            if (hasNext) {
                throw new NoSuchElementException();
            }
            hasNext = true;
            return (E)data;
        }

        @Override
        public int nextIndex() {
            if (hasNext) {
                return 0;
            }
            return -1;
        }

        @Override
        public int previousIndex() {
            if (!hasNext) {
                return 0;
            }
            return -1;
        }

        @Override
        public void remove() {

        }

        @Override
        public void set(E e) {
            data = e;
        }

        @Override
        public void add(E e) {

        }
    }

    private class FiveOrLessElementIterator implements ListIterator<E>  {

        private int index = 0;
        @Override
        public boolean hasNext() {
            return index < 5;
        }

        @Override
        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            index++;
            return (E)((Object[]) data)[index - 1];
        }

        @Override
        public boolean hasPrevious() {
            return index > 0;
        }

        @Override
        public E previous() throws NoSuchElementException {
            if (!hasPrevious()) {
                throw new NoSuchElementException();
            }
            index--;
            return (E)((Object[]) data)[index + 1];
        }

        @Override
        public int nextIndex() {
            return index;
        }

        @Override
        public int previousIndex() {
            return index - 1;
        }

        @Override
        public void remove() {

        }

        @Override
        public void set(E e) {
            ((Object[]) data)[index] = e;
        }

        @Override
        public void add(E e) {

        }
    }

    private class SixOrMoreElementIterator implements ListIterator<E>  {

        private int index = 0;
        @Override
        public boolean hasNext() {
            return index < size;
        }

        @Override
        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            index++;
            return ((ArrayList<E>)data).get(index);
        }

        @Override
        public boolean hasPrevious() {
            return false;
        }

        @Override
        public E previous() {
            return null;
        }

        @Override
        public int nextIndex() {
            return 0;
        }

        @Override
        public int previousIndex() {
            return 0;
        }

        @Override
        public void remove() {

        }

        @Override
        public void set(E e) {

        }

        @Override
        public void add(E e) {

        }
    }

    @NotNull
    @Override
    public Iterator<E> iterator() {
        return listIterator();
    }

    @NotNull
    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @NotNull
    @Override
    public <T> T[] toArray(@NotNull T[] a) {
        return null;
    }

    @Override
    public boolean add(E e) {
        size++;
        if (size == 1) {
            data = e;
        } else if (size == 2) {
            Object[] array = new Object[5];
            array[0] = data;
            array[1] = e;
            data = array;
        } else if (size <= 5) {
            ((Object[])data)[size - 1] = e;
        } else if (size == 6) {
            List<E> list = new ArrayList<E>(6);
            for (int i = 0; i < 5; ++i){
                list.add((E)((Object[]) data)[i]);
            }
            data = list;
        } else {
            ((ArrayList<E>)data).add(e);
        }
        return true;
    }

    @Override
    public boolean remove(Object o) {
        if (!contains(o)) {
            return false;
        }
        size--;
        if (size == 0) {
            data = null;
        } else if (size == 1) {
            E e;
            if (((E)((Object[]) data)[0]).equals(o)) {
                e = (E)((Object[]) data)[1];
            } else {
                e = (E)((Object[]) data)[0];
            }
            data = e;
        } else if (size <= 4) {
            Object[] array = new Object[5];
            int index = 0;
            for (int i = 0; i < size + 1; ++i) {
                if (!((E)((Object[]) data)[i]).equals(o)) {
                    array[index] = (E)((Object[]) data)[i];
                    index++;
                }
            }
            data = array;
        } else if (size == 5) {
            Object[] array = new Object[5];
            int index = 0;
            for (int i = 0; i < 5; ++i){
                if (!((ArrayList<E>) data).get(i).equals(o)) {
                    array[i] = ((ArrayList<E>) data).get(i);
                    index++;
                }
            }
        } else {
            ((ArrayList<E>) data).remove(o);
        }
        return true;
    }

    @Override
    public boolean containsAll(@NotNull Collection<?> c) {
        for (Object e : c) {
            if (!contains(e)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends E> c) {
        for (E e : c) {
            add(e);
        }
        return true;
    }

    @Override
    public boolean addAll(int index, @NotNull Collection<? extends E> c) {
        return false;
    }

    @Override
    public boolean removeAll(@NotNull Collection<?> c) {
        return false;
    }

    @Override
    public boolean retainAll(@NotNull Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {

    }

    @Override
    public E get(int index) throws IndexOutOfBoundsException {
        if (index < 0 || size <= index) {
            throw new IndexOutOfBoundsException();
        } else if (size == 1) {
            return (E)data;
        } else if (size <= 5) {
            return (E)((Object[]) data)[index];
        } else {
            return ((ArrayList<E>) data).get(index);
        }
    }

    @Override
    public E set(int index, E element) throws IndexOutOfBoundsException {
        E old;
        if (index < 0 || size <= index) {
            throw new IndexOutOfBoundsException();
        } else if (size == 1) {
            old = ((E)(data));
            data = element;
        } else if (size <= 5) {
            old = (E)((Object[])data)[index];
            ((Object[])data)[index] = element;
        } else {
            old = ((ArrayList<E>) data).get(index);
            ((ArrayList<E>) data).set(index, element);
        }
        return old;
    }

    @Override
    public void add(int index, E element) {

    }

    @Override
    public E remove(int index) {
        return null;
    }

    @Override
    public int indexOf(Object o) {
        return 0;
    }

    @Override
    public int lastIndexOf(Object o) {
        return 0;
    }

    @NotNull
    @Override
    public ListIterator<E> listIterator() {
        if (size == 0) {
            return new NoneElementIterator();
        }
        if (size == 1) {
            return new OneElementIterator();
        }
        if (size <= 5) {
            return new FiveOrLessElementIterator();
        }
        return new SixOrMoreElementIterator();
    }

    @NotNull
    @Override
    public ListIterator<E> listIterator(int index) {
        return null;
    }

    @NotNull
    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        return null;
    }
}