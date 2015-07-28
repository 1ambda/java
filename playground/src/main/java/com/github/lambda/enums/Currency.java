package com.github.lambda.enums;

// ref : http://javarevisited.blogspot.kr/2011/08/enum-in-java-example-tutorial.html
public enum Currency {
    PENNY(1), NICKLE(5), DIME(10), QUARTER(25);

    private int value;

    public int getValue() { return this.value; }

    private Currency(int value) {
        this.value = value;
    }
}
