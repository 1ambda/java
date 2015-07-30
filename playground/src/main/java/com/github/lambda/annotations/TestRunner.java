package com.github.lambda.annotations;

import java.lang.reflect.*;

public class TestRunner {
    static private int tests = 0;
    static private int passed = 0;
    public static void main(String[] args) throws ClassNotFoundException {

        String testSuiteClassName = "com.github.lambda.annotations.SampleTestSuite";
        runSuite(testSuiteClassName);
    }

    private static void runSuite(String suiteName) throws ClassNotFoundException {
        Class testClass = Class.forName(suiteName);

        for (Method m : testClass.getDeclaredMethods()) {
            if (m.isAnnotationPresent(Test.class)) {
                runTest(m);
            } else if (m.isAnnotationPresent(ExceptionTest.class)) {
                runExceptionTest(m);
            } else if (m.isAnnotationPresent(MultipleExceptionTest.class)) {
                runMultipleExceptionTest(m);
            }

        }

        System.out.printf("Passed: %d, Failed %d\n", passed, tests - passed);
    }

    public static void runTest(Method m) {
        tests++;

        try {
            m.invoke(null);
            passed++;
        } catch (InvocationTargetException wrappedEx) {
            Throwable t = wrappedEx.getCause();

            // check if ExceptionTest
            Class<? extends Exception> exType = m.getAnnotation(ExceptionTest.class).value();

            if (exType.isInstance(t)) {
                passed++;
            } else {
                System.out.printf("ExceptionTest %s failed: expected %s, got %s\n",
                        m, exType.getName(), t);
            }

        } catch (Exception e) {
            // not static method
            System.out.println("INVALID @Test: " + m);
        }
    }

    public static void runExceptionTest(Method m) {
        tests++;

        try {
            m.invoke(null);
            System.out.printf("ExceptionTest %s failed: No exception\n", m);
        } catch (InvocationTargetException wrappedEx) {
            Throwable t = wrappedEx.getCause();

            Class<? extends Exception> exType = m.getAnnotation(ExceptionTest.class).value();

            if (exType.isInstance(t)) {
                passed++;
            } else {
                System.out.printf("ExceptionTest %s failed: expected %s, got %s\n",
                        m, exType.getName(), t);
            }

        } catch (Exception e) {
            // not static method
            System.out.println("INVALID @Test: " + m);
        }
    }

    public static void runMultipleExceptionTest(Method m) {
        tests++;

        try {
            m.invoke(null);
            System.out.printf("MultipleExceptionTest %s failed: No exception\n", m);
        } catch (InvocationTargetException wrappedEx) {
            Throwable t = wrappedEx.getCause();

            Class<? extends Exception>[] exTypes = m.getAnnotation(MultipleExceptionTest.class).value();
            int oldPassed = passed;

            for (Class<? extends Exception> exType : exTypes) {
                if (exType.isInstance(t)) {
                    passed++;
                    break;
                }
            }

            if (oldPassed == passed) {
                System.out.printf("MultipleExceptionTest %s failed: %s\n", m, t);
            }


        } catch (Exception e) {
            // not static method
            System.out.println("INVALID @Test: " + m);
        }
    }
}
