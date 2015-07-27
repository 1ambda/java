package com.github.lambda.serialization;

import java.io.Serializable;

// ref: http://www.tutorialspoint.com/java/java_serialization.htm
public class Employee implements Serializable {
    public String name;
    public String address;
    public int number;
    transient public int SSN;
    public void mailCheck() {
        System.out.printf("Mailing a check to " + name + " " + address);
    }

    @Override
    public String toString() {
        return "Employee{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", number=" + number +
                ", SSN=" + SSN +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Employee employee = (Employee) o;

        if (number != employee.number) return false;
        if (SSN != employee.SSN) return false;
        if (!name.equals(employee.name)) return false;
        return address.equals(employee.address);

    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + address.hashCode();
        result = 31 * result + number;
        result = 31 * result + SSN;
        return result;
    }
}
