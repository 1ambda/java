package com.github.lambda.enums;

import java.util.HashMap;
import java.util.Map;

public enum OperationEnum {
    PLUS("+") { @Override double apply(double x, double y) { return x + y; } },
    MINUS("-") { @Override double apply(double x, double y) { return x - y; } },
    TIMES("*") { @Override double apply(double x, double y) { return x * y; } },
    DIVIDE("/") { @Override double apply(double x, double y) { return x / y; } };

    private String symbol;

    OperationEnum(String symbol) { this.symbol = symbol; }

    @Override public String toString() { return this.symbol; }

    abstract double apply(double x, double y);

    // fromString
    private static final Map<String, OperationEnum> stringToEnum = new HashMap<>();
    static { for (OperationEnum op : OperationEnum.values()) stringToEnum.put(op.toString(), op); }
    public static OperationEnum fromString(String symbol) { return stringToEnum.get(symbol); }
}
