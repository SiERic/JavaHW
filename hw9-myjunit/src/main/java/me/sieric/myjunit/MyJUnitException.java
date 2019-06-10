package me.sieric.myjunit;

/**
 * Exception which is thrown then invoker meets incorrect
 * combination of annotations
 */
public class MyJUnitException extends Exception {

    public MyJUnitException(String s) {
        super(s);
    }
}
