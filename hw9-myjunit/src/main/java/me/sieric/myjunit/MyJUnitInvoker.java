package me.sieric.myjunit;

import me.sieric.myjunit.annotations.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Class to invoke all MyJUnit tests in the given class
 * Methods with BeforeClass annotation will be invoked before all test methods
 * Methods with AfterClass annotation will be invoked after all test methods
 * Methods with Before annotation will be invoked before each test method
 * Methods with After annotation will be invoked after each test method
 * Test class should have default constructor
 * Each test method shouldn't take any arguments
 */
public class MyJUnitInvoker {

    /**
     * Console application that runs MyJUnit test invoker
     * Takes path to the java class with tests as an argument
     * Prints information about tests status and execution time
     * @param args path to the tests class
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Wrong argument format. Pass the path to the directory with .class files");
            return;
        }

        Class testClass;

        try {
            testClass = Class.forName(args[0]);
        } catch (ClassNotFoundException e) {
            System.out.println("Class was not found");
            return;
        }
        try {
            run(testClass);
        } catch (InstantiationException | IllegalAccessException | MyJUnitException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Runs all MyJUnit tests annotated in {@code testClass}
     * @param testClass given class
     * @return test execution status
     * @throws InstantiationException if test class instance creating failed
     * @throws IllegalAccessException if private method was called
     * @throws MyJUnitException if annotations are incorrect
     */
    public static TestStatus run(Class testClass) throws InstantiationException, IllegalAccessException, MyJUnitException {
        ClassMethods methods = getMethods(testClass);
        return invoke(methods);
    }

    /**
     * Finds all annotated in {@code testClass} methods
     * @param testClass given class
     * @return annotated methods
     * @throws IllegalAccessException if private method was called
     * @throws InstantiationException if test class instance creating failed
     * @throws MyJUnitException if annotations are incorrect
     */
    private static ClassMethods getMethods(Class testClass) throws IllegalAccessException, InstantiationException, MyJUnitException {
        ClassMethods classMethods = new ClassMethods();
        classMethods.instance = testClass.newInstance();
        for (var method : testClass.getDeclaredMethods()) {
            int cntAnnotations = 0;
            method.setAccessible(true);
            if (method.getAnnotation(Test.class) != null) {
                Test test = method.getAnnotation(Test.class);
                classMethods.tests.add(new TestMethod(method, test.expected(), test.ignore()));
                cntAnnotations++;
            }
            if (method.getAnnotation(Before.class) != null) {
                classMethods.before.add(method);
                cntAnnotations++;
            }
            if (method.getAnnotation(After.class) != null) {
                classMethods.after.add(method);
                cntAnnotations++;
            }
            if (method.getAnnotation(BeforeClass.class) != null) {
                classMethods.beforeClass.add(method);
                cntAnnotations++;
            }
            if (method.getAnnotation(AfterClass.class) != null) {
                classMethods.afterClass.add(method);
                cntAnnotations++;
            }

            if (cntAnnotations > 1) {
                throw new MyJUnitException("More than one MyJUnit annotation found at the same method " + method.getName());
            }
        }
        return classMethods;
    }

    /**
     * Invokes all annotated methods
     * @param classMethods given methods
     * @return tests execution status
     */
    private static TestStatus invoke(ClassMethods classMethods) {
        TestStatus status = new TestStatus();
        long superStartTime = System.currentTimeMillis();
        System.out.println("===");
        try {
            for (var method : classMethods.beforeClass) {
                method.invoke(classMethods.instance);
            }
            for (var test : classMethods.tests) {
                if (!test.ignore.equals(Test.EMPTY)) {
                    System.out.printf("Test %s ignored\nReason: %s\n", test.test.getName(), test.ignore);
                    status.ignored++;
                    continue;
                }
                for (var method : classMethods.before) {
                    method.invoke(classMethods.instance);
                }
                long startTime = System.currentTimeMillis();

                boolean wasException = false;
                try {
                    test.test.invoke(classMethods.instance);
                } catch (Exception e) {
                    if (!e.getCause().getClass().equals(test.expected)) {
                        System.out.printf("Test %s failed\nReason: %s\nTime: %.3f sec\n",
                                test.test.getName(), e.getCause().getMessage(), (System.currentTimeMillis() - startTime) / 1000.0);
                        status.failed++;
                    } else {
                        System.out.printf("Test %s passed\nTime: %.3f sec\n", test.test.getName(), (System.currentTimeMillis() - startTime) / 1000.0);
                        status.passed++;
                    }
                    wasException = true;
                }
                if (!wasException) {
                    if (test.expected.equals(Test.None.class)) {
                        System.out.printf("Test %s passed\nTime: %.3f sec\n", test.test.getName(), (System.currentTimeMillis() - startTime) / 1000.0);
                        status.passed++;
                    } else {
                        System.out.printf("Test %s failed\nReason: Exception expected: %s\nTime: %.3f sec\n",
                                test.test.getName(), test.expected.getName(), (System.currentTimeMillis() - startTime) / 1000.0);
                        status.failed++;
                    }
                }
                for (var method : classMethods.after) {
                    method.invoke(classMethods.instance);
                }
            }
            for (var method : classMethods.afterClass) {
                method.invoke(classMethods.instance);
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            System.out.println("Exception occurred:");
            System.out.println(e.getMessage());
        }
        System.out.println("===");
        System.out.printf("Passed: %d\n", status.passed);
        System.out.printf("Failed: %d\n", status.failed);
        System.out.printf("Ignored: %d\n", status.ignored);
        System.out.printf("Total time: %.3f sec\n", (System.currentTimeMillis() - superStartTime) / 1000.0);
        return status;
    }

    /**
     * Class to store MyJUnit Test method
     * Contain class itself and annotation arguments: expected exception type and ignore reason
     */
    private static class TestMethod {
        private Method test;
        private Class expected;
        private String ignore;

        TestMethod(Method test, Class expected, String ignore) {
            this.test = test;
            this.expected = expected;
            this.ignore = ignore;
        }
    }

    /**
     * Class to store all annotated in class methods
     * Contains lists of methods, divided by their type
     */
    private static class ClassMethods {
        private Object instance = null;
        private List<TestMethod> tests = new ArrayList<>();
        private List<Method> before = new ArrayList<>();
        private List<Method> after = new ArrayList<>();
        private List<Method> beforeClass = new ArrayList<>();
        private List<Method> afterClass = new ArrayList<>();
    }

    /**
     * Class to store test execution status
     * Contain number of passed, failed and ignored tests
     */
    static class TestStatus {
        int passed;
        int failed;
        int ignored;
    }
}
