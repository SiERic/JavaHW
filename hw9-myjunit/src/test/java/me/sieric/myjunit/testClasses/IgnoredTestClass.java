package me.sieric.myjunit.testClasses;

import me.sieric.myjunit.annotations.Test;

public class IgnoredTestClass {

    @Test(ignore = "just don't like it")
    void test() {}
}
