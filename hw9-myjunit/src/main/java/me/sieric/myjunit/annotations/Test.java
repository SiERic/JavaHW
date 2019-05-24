package me.sieric.myjunit.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for test methods
 * Test can be ignored if {@code ignore} argument present and is not empty
 * If the test method should throw an exception, it can be checked by
 * setting {@code expected} to class of the desired exception
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Test {

    /**
     * Returns a throwable which is expected to be thrown from test
     * @return a throwable which is expected to be thrown from test
     */
    Class<? extends Throwable> expected() default None.class;

    /**
     * Returns an ignore reason
     * @return an ignore reason
     */
    String ignore() default EMPTY;

    /** Default empty exception */
    class None extends Throwable {}

    /** Default empty ignore reason */
    String EMPTY = "";
}