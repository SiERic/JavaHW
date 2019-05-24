package me.sieric.myjunit.testClasses;

import me.sieric.myjunit.annotations.AfterClass;
import me.sieric.myjunit.annotations.BeforeClass;

public class BeforeAfterClass {

    @BeforeClass
    void before() {
        TestClass.beforeClassFlag = true;
    }

    @AfterClass
    void after() {
        TestClass.afterClassFlag = true;
    }
}
