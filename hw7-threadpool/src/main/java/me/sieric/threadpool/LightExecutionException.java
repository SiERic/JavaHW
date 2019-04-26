package me.sieric.threadpool;

/**
 * Exception for {@link LightFuture} execution
 */
public class LightExecutionException extends Exception {
    LightExecutionException(Exception e) {
        super(e);
    }
}
