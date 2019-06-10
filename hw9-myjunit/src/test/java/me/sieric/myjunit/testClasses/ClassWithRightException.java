package me.sieric.myjunit.testClasses;

import me.sieric.myjunit.annotations.Test;

public class ClassWithRightException {

    @Test(expected = SpecialException.class)
    void test() throws SpecialException {
        throw new SpecialException();
    }

    private class SpecialException extends Exception {}
}
