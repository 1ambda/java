package com.github.lambda.realnumbers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static junit.framework.Assert.*;

@RunWith(JUnit4.class)
public class RealNumberTest {

    @Test
    public void test() {
        System.out.println(1.03 - .42);
        assertNotSame(.61, 1.03 - .42);

        System.out.println(1.00 - 9 * .10);
    }
}
