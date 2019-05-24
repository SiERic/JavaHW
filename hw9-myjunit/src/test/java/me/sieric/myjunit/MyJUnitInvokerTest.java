package me.sieric.myjunit;

import me.sieric.myjunit.testClasses.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MyJUnitInvokerTest {

    @Test
    void testEmptyClass() {
        MyJUnitInvoker.TestStatus status = null;
        try {
            status = MyJUnitInvoker.run(EmptyClass.class);
        } catch (InstantiationException | MyJUnitException | IllegalAccessException e) {
            fail("Exception thrown");
        }
        assertEquals(0, status.failed);
        assertEquals(0, status.passed);
        assertEquals(0, status.ignored);
    }

    @Test
    void testPass() {
        MyJUnitInvoker.TestStatus status = null;
        try {
            status = MyJUnitInvoker.run(PassClass.class);
        } catch (InstantiationException | MyJUnitException | IllegalAccessException e) {
            fail("Exception thrown");
        }
        assertEquals(0, status.failed);
        assertEquals(1, status.passed);
        assertEquals(0, status.ignored);
    }

    @Test
    void testIgnore() {
        MyJUnitInvoker.TestStatus status = null;
        try {
            status = MyJUnitInvoker.run(IgnoredTestClass.class);
        } catch (InstantiationException | MyJUnitException | IllegalAccessException e) {
            fail("Exception thrown");
        }
        assertEquals(0, status.failed);
        assertEquals(0, status.passed);
        assertEquals(1, status.ignored);
    }

    @Test
    void testFail() {
        MyJUnitInvoker.TestStatus status = null;
        try {
            status = MyJUnitInvoker.run(FailClass.class);
        } catch (InstantiationException | MyJUnitException | IllegalAccessException e) {
            fail("Exception thrown");
        }
        assertEquals(1, status.failed);
        assertEquals(0, status.passed);
        assertEquals(0, status.ignored);
    }

    @Test
    void testCheckException() {
        MyJUnitInvoker.TestStatus status = null;
        try {
            status = MyJUnitInvoker.run(ClassWithRightException.class);
        } catch (InstantiationException | MyJUnitException | IllegalAccessException e) {
            fail("Exception thrown");
        }
        assertEquals(0, status.failed);
        assertEquals(1, status.passed);
        assertEquals(0, status.ignored);
    }

    @Test
    void testCheckExceptionFailed() {
        MyJUnitInvoker.TestStatus status = null;
        try {
            status = MyJUnitInvoker.run(ClassWithoutRightException.class);
        } catch (InstantiationException | MyJUnitException | IllegalAccessException e) {
            fail("Exception thrown");
        }
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
    void testBeforeAfter() {
        TestClass.before = 0;
        TestClass.after = 0;
        MyJUnitInvoker.TestStatus status = null;
        try {
            status = MyJUnitInvoker.run(BeforeAfter.class);
        } catch (InstantiationException | MyJUnitException | IllegalAccessException e) {
            fail("Exception thrown");
        }
        assertEquals(0, status.failed);
        assertEquals(3, status.passed);
        assertEquals(0, status.ignored);
        assertEquals(3, TestClass.before);
        assertEquals(3, TestClass.after);
    }
}