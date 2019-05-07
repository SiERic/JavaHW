package me.sieric.threadpool;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Thread pool with constant number of threads.
 */
public class ThreadPool {

    private Thread[] threads;
    private SynchronizedQueue<ThreadPoolTask<?>> queue = new SynchronizedQueue<>();
    private boolean isShutDown;

    public ThreadPool(int threadsNumber) {
        threads = new Thread[threadsNumber];
        for (int i = 0; i < threadsNumber; i++) {
            threads[i] = new Thread(() -> {
                try {
                    while (!Thread.interrupted()) {
                        ThreadPoolTask<?> lightFuture = queue.pop();
                        lightFuture.calculate();
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
            threads[i].start();
        }
    }

    /**
     * Adds new task to thread pool
     * @param supplier task
     * @param <T> type of return value
     * @return {@link LightFuture} to track progress of task
     * @throws IllegalStateException if called after shutdown
     */
    public <T> LightFuture<T> add(Supplier<T> supplier) {
        if (isShutDown) {
            throw new IllegalStateException("Pool was shut down so no more tasks accepted");
        }
        ThreadPoolTask<T> lightFuture = new ThreadPoolTask<>(supplier);
        queue.push(lightFuture);
        return lightFuture;
    }

    /**
     * Interrupts all threads in the pool
     */
    public void shutdown() throws InterruptedException {
        isShutDown = true;
        for (Thread thread : threads) {
            thread.interrupt();
        }
        for (Thread thread : threads) {
            thread.join();
        }
    }


    /**
     * {@link LightFuture} implementation for thread pool
     */
    private class ThreadPoolTask<T> implements LightFuture<T> {

        private Supplier<T> supplier;
        private volatile boolean isReady = false;
        private T result;
        private Exception exception;

        public ThreadPoolTask(Supplier<T> supplier) {
            this.supplier = supplier;
        }

        @Override
        public boolean isReady() {
            return isReady;
        }

        @Override
        public synchronized T get() throws LightExecutionException {
            while (!isReady) {
                Thread.yield();
            }
            if (exception != null) {
                throw new LightExecutionException(exception);
            }
            return result;
        }

        @Override
        public <R> LightFuture<R> thenApply(Function<? super T, R> function) {
            return add(() -> {
                try {
                    return function.apply(get());
                } catch (LightExecutionException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        private void calculate() {
            try {
                result = supplier.get();
            } catch (Exception e) {
                exception = e;
            }
            supplier = null;
            isReady = true;
        }
    }
}
