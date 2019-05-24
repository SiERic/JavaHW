package me.sieric.myjunit.testClasses;

import me.sieric.myjunit.annotations.Test;

public class ClassWithoutRightException {

    @Test(expected = SpecialException.class)
    void test() {}

    private class SpecialException extends Exception {}
}
