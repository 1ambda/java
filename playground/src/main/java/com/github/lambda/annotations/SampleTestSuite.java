package com.github.lambda.annotations;

import java.util.ArrayList;
import java.util.List;

public class SampleTestSuite {

    @Test
    public static void success1() {}

    @ExceptionTest(RuntimeException.class)
    public static void success2() {
        throw new RuntimeException();
    }

    @ExceptionTest(ArithmeticException.class)
    public static void success3() {
        int i = 0;
        int b = i / i;
    }

    @MultipleExceptionTest({
            IndexOutOfBoundsException.class,
            NullPointerException.class
    })
    public static void success4() {
        List<String> list = new ArrayList<>();

        list.addAll(5, null);
    }

    // failure tests
    @Test
    public void failure1() { throw new RuntimeException(); }

    @ExceptionTest(Exception.class)
    public static void failure2() {}

    @MultipleExceptionTest({
            IndexOutOfBoundsException.class,
            NullPointerException.class
    })
    public static void failure3() { }
}
