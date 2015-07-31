package com.github.lambda.enums;

public enum ExtendedOperation implements Operation{
    EXP("^") { @Override public double apply(double x, double y) { return Math.pow(x, y); } },
    REMINDER("%") { @Override public double apply(double x, double y) { return x % y; } };


    private String symbol;
    ExtendedOperation(String symbol) { this.symbol = symbol; }
    @Override public String toString() { return this.symbol; }
}
