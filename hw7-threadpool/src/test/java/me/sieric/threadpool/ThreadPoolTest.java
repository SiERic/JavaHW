package me.sieric.threadpool;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.LinkedList;

import static java.lang.Integer.max;
import static org.junit.jupiter.api.Assertions.*;

class ThreadPoolTest {

    private ThreadPool pool;

    @Test
    void testOneTaskOneAsk() throws LightExecutionException {
        pool = new ThreadPool(1);
        LightFuture<Integer> future = pool.add(() -> 6 * 7);
        assertEquals(Integer.valueOf(42), future.get());
    }

    @RepeatedTest(20)
    void testManyTasks() throws LightExecutionException {
        pool = new ThreadPool(3);
        LinkedList<LightFuture<Integer> > lightFutures = new LinkedList<>();

        final int N = 100;
        for (int i = 0; i < N; i++) {
            int finalI = i;
            lightFutures.add(pool.add(() -> finalI));
        }

        for (int i = 0; i < N; i++) {
            LightFuture<Integer> lightFuture = lightFutures.get(i);
            assertEquals(Integer.valueOf(i), lightFuture.get());
        }
    }

    @RepeatedTest(20)
    void testManyTasksWithoutFuture() throws InterruptedException {
        pool = new ThreadPool(3);
        final int N = 100;
        int[] number = new int[N];
        for (int i = 0; i < N; i++) {
            int finalI = i;
            pool.add(() -> ++number[finalI]);
        }

        Thread.sleep(100);

        for (int i = 0; i < N; i++) {
            assertEquals(1, number[i]);
        }
    }

    @RepeatedTest(200)
    void testApplyAfter() throws LightExecutionException {
        pool = new ThreadPool(10);

        final int N = 10;
        ArrayList<LightFuture<Integer> > tasks = new ArrayList<>();
        tasks.add(pool.add(() -> 1));
        for (int i = 1; i < N; i++) {
            tasks.add(tasks.get(i - 1).thenApply(x -> x * 2));
        }

        for (int i = 0; i < N; i++) {
            assertEquals(Integer.valueOf(1 << i), tasks.get(i).get());
        }
    }

    @Test
    void testApplyAfterWaiting() throws InterruptedException, LightExecutionException {
        pool = new ThreadPool(1);
        LightFuture<Integer> task1 = pool.add(() -> {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return 2;
        });
        LightFuture<String> task2 = task1.thenApply(x -> "4" + x);
        assertFalse(task1.isReady());
        assertFalse(task2.isReady());
        Thread.sleep(300);
        pool.shutdown();
        assertTrue(task1.isReady());
        assertEquals("42", task2.get());
    }

    @RepeatedTest(20)
    void testShutDown() throws InterruptedException {
        ThreadGroup threadGroup = Thread.currentThread().getThreadGroup();
        int threadsOnStart = threadGroup.activeCount();
        final int N = 100;
        pool = new ThreadPool(N);

        for (int i = 0; i < N * N; i++) {
            pool.add(() -> 6 * 7);
        }

        pool.shutdown();
        assertEquals(threadsOnStart, threadGroup.activeCount());
        assertThrows(IllegalStateException.class, () -> pool.add(() -> 42));
    }

    private int maxThreads;

    @RepeatedTest(20)
    void testCountThreads() throws InterruptedException {
        ThreadGroup threadGroup = Thread.currentThread().getThreadGroup();
        int threadsOnStart = threadGroup.activeCount();
        final int N = 100;
        pool = new ThreadPool(N);
        maxThreads = threadsOnStart;

        for (int i = 0; i < N * N; i++) {
            pool.add(() -> {
                synchronized ("Counting threads") {
                    maxThreads = max(maxThreads, Thread.currentThread().getThreadGroup().activeCount());
                }
                return 42;
            });
        }
        Thread.sleep(100);
        assertTrue(maxThreads - threadsOnStart <= N);
    }

    @Test
    void testExceptions() throws LightExecutionException {
        pool = new ThreadPool(5);
        LightFuture<Integer> task1 = pool.add(() -> null);
        LightFuture<Integer> task2 = task1.thenApply(a -> a * 2);
        assertNull(task1.get());
        assertThrows(LightExecutionException.class, task2::get);
    }

}