package me.sieric.threadpool;

import java.util.function.Function;

/**
 * An interface for ThreadPoll task wrapper
 * @param <T> type of task return value
 */
public interface LightFuture<T> {

    /**
     * Returns if task is ready
     * @return true if task is ready, false otherwise
     */
    boolean isReady();

    /**
     * Waits for calculating and returns the result
     * @return the result of task calculating
     * @throws LightExecutionException if exception occurred during calculation
     */
    T get() throws LightExecutionException;

    /**
     * Applies function to the result of task to get a new task
     * @param function a function to apply
     * @param <R> type of new task return value
     * @return result of application (a new task)
     */
    <R> LightFuture<R> thenApply(Function<? super T, R> function);
}
