package me.sieric.myjunit.testClasses;

import me.sieric.myjunit.annotations.After;
import me.sieric.myjunit.annotations.Before;
import me.sieric.myjunit.annotations.Test;

public class BeforeAfter {

    @Before
    void before() {
        TestClass.before++;
    }

    @Test
    void test1() {}

    @Test
    void test2() {}

    @Test
    void test3() {}

    @After
    void after() {
        TestClass.after++;
    }
}
