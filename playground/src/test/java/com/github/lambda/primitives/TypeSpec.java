package com.github.lambda.primitives;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Comparator;

import static junit.framework.Assert.*;

@RunWith(JUnit4.class)
public class TypeSpec {

    @Test
    public void test(){
        Long l1 = new Long(3L);
        Long l2 = new Long(3L);

        assertFalse(l1 == l2);

        Long l3 = 3L;
        Long l4 = 3L;

        assertTrue(l3 == l4);
    }

    @Test
    public void test_Comparator() {
        Comparator<Integer> naturalOrdering = new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1 < o2 ? -1 : (o1 == o2 ? 0 : 1);
            }
        };

        assertEquals(-1, naturalOrdering.compare(new Integer(41), new Integer(42)));
        assertNotSame(0, naturalOrdering.compare(new Integer(42), new Integer(42)));
    }

    @Test
    public void test_Comparator2() {
        Comparator<Integer> naturalOrdering = new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                int x = o1;
                int y = o2;
                return x < y ? -1 : (x == y ? 0 : 1);
            }
        };

        assertEquals(-1, naturalOrdering.compare(new Integer(41), new Integer(42)));
        assertEquals(0, naturalOrdering.compare(new Integer(42), new Integer(42)));
    }
}
