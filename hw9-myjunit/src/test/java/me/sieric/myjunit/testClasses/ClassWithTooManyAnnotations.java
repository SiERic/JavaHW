package me.sieric.myjunit.testClasses;

import me.sieric.myjunit.annotations.After;
import me.sieric.myjunit.annotations.Before;

public class ClassWithTooManyAnnotations {
    @Before
    @After
    void kek() {}
}
