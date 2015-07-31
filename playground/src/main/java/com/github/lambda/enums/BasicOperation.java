package com.github.lambda.enums;

import java.util.HashMap;

public enum BasicOperation implements Operation{
    PLUS("+") { @Override public double apply(double x, double y) { return x + y; } },
    MINUS("-") { @Override public double apply(double x, double y) { return x - y; } },
    TIMES("*") { @Override public double apply(double x, double y) { return x * y; } },
    DIVIDE("/") { @Override public double apply(double x, double y) { return x / y; } };

    private String symbol;
    BasicOperation(String symbol) { this.symbol = symbol; }
    @Override public String toString() { return this.symbol; }
}
