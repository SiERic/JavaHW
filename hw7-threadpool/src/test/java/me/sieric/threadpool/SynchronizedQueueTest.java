package me.sieric.threadpool;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class SynchronizedQueueTest {
    private SynchronizedQueue<Integer> queue;

    @BeforeEach
    void setUp() {
        queue = new SynchronizedQueue<>();
    }

    @Test
    void testSingleThread() throws InterruptedException {
        queue.push(42);
        queue.push(239);
        assertEquals(Integer.valueOf(42), queue.pop());
        assertEquals(Integer.valueOf(239), queue.pop());
    }

    @RepeatedTest(20)
    void testMultiThread() throws InterruptedException {
        final int N = 1000;
        ArrayList<Integer> numbers = new ArrayList<>();
        ArrayList<Integer> takenNumbers = new ArrayList<>();
        Thread[] producers = new Thread[N];
        Thread[] consumers = new Thread[N];
        for (int i = 0; i < N; i++) {
            numbers.add(i);
            int finalI = i;
            producers[i] = new Thread(() -> queue.push(finalI));
            consumers[i] = new Thread(() -> {
                try {
                    synchronized (takenNumbers) {
                        takenNumbers.add(queue.pop());
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
            producers[i].start();
            consumers[i].start();
        }
        for (int i = 0; i < N; i++) {
            producers[i].join();
            consumers[i].join();
        }
        assertEquals(N, takenNumbers.size());
        assertTrue(takenNumbers.containsAll(numbers));
    }
}