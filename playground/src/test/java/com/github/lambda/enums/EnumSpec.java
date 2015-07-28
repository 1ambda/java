package com.github.lambda.enums;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnit4.class)
public class EnumSpec {

    @Test
    public void test_Currency() {
        Currency c1 = Currency.PENNY;

        assertThat(c1.getValue()).isEqualTo(1);
        assertThat(Currency.valueOf("PENNY")).isEqualTo(Currency.PENNY);
    }

}
