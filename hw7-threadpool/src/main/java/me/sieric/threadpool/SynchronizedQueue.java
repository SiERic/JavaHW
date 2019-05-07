package me.sieric.threadpool;

import java.util.LinkedList;

/**
 * Synchronized queue. Makes thread wait for element to appear in queue
 * @param <E> type of stored elements
 */
public class SynchronizedQueue<E> {
    private final LinkedList<E> queue = new LinkedList<>();

    /**
     * Adds new element
     * @param e element to add
     */
    public void push(E e) {
        synchronized (queue) {
            queue.add(e);
            queue.notifyAll();
        }
    }

    /**
     * Returns (and remove) head element of the queue.
     * If queue is empty, waits for element to be added
     * @return an element
     * @throws InterruptedException if the thread was interrupted while waiting for new element
     */
    public E pop() throws InterruptedException {
        synchronized (queue) {
            while (queue.isEmpty()) {
                queue.wait();
            }
            return queue.remove(0);
        }
    }
}
