package me.sieric.myjunit;

import me.sieric.myjunit.testClasses.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MyJUnitInvokerTest {

    @Test
    void testEmptyClass() throws IllegalAccessException, MyJUnitException, InstantiationException {
        MyJUnitInvoker.TestStatus status = MyJUnitInvoker.run(EmptyClass.class);
        assertEquals(0, status.failed);
        assertEquals(0, status.passed);
        assertEquals(0, status.ignored);
    }

    @Test
    void testPass() throws IllegalAccessException, MyJUnitException, InstantiationException {
        MyJUnitInvoker.TestStatus status = MyJUnitInvoker.run(PassClass.class);
        assertEquals(0, status.failed);
        assertEquals(1, status.passed);
        assertEquals(0, status.ignored);
    }

    @Test
    void testIgnore() throws IllegalAccessException, MyJUnitException, InstantiationException {
        MyJUnitInvoker.TestStatus status = MyJUnitInvoker.run(IgnoredTestClass.class);
        assertEquals(0, status.failed);
        assertEquals(0, status.passed);
        assertEquals(1, status.ignored);
    }

    @Test
    void testFail() throws IllegalAccessException, MyJUnitException, InstantiationException {
        MyJUnitInvoker.TestStatus status = MyJUnitInvoker.run(FailClass.class);
        assertEquals(1, status.failed);
        assertEquals(0, status.passed);
        assertEquals(0, status.ignored);
    }

    @Test
    void testCheckException() throws IllegalAccessException, MyJUnitException, InstantiationException {
        MyJUnitInvoker.TestStatus status = MyJUnitInvoker.run(ClassWithRightException.class);
        assertEquals(0, status.failed);
        assertEquals(1, status.passed);
        assertEquals(0, status.ignored);
    }

    @Test
    void testCheckExceptionFailed() throws IllegalAccessException, MyJUnitException, InstantiationException {
        MyJUnitInvoker.TestStatus status = MyJUnitInvoker.run(ClassWithoutRightException.class);
        assertEquals(1, status.failed);
        assertEquals(0, status.passed);
        assertEquals(0, status.ignored);
    }

    @Test
    void testTooManyAnnotations() {
        assertThrows(MyJUnitException.class, () -> MyJUnitInvoker.run(ClassWithTooManyAnnotations.class));
    }

    @Test
    void testBeforeAfterClass() {
        TestClass.beforeClassFlag = false;
        TestClass.afterClassFlag = false;
        assertAll(() -> MyJUnitInvoker.run(BeforeAfterClass.class));
        assertTrue(TestClass.beforeClassFlag);
        assertTrue(TestClass.afterClassFlag);
    }

    @Test
    void testBeforeAfter() throws IllegalAccessException, MyJUnitException, InstantiationException {
        TestClass.before = 0;
        TestClass.after = 0;
        MyJUnitInvoker.TestStatus status = MyJUnitInvoker.run(BeforeAfter.class);
        assertEquals(0, status.failed);
        assertEquals(3, status.passed);
        assertEquals(0, status.ignored);
        assertEquals(3, TestClass.before);
        assertEquals(3, TestClass.after);
    }
}