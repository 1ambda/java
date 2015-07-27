package com.github.lambda.serialization;

import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import sun.nio.ch.IOUtil;

import java.io.*;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnit4.class)
public class SerializableSpec {

    private final String FILE_NAME = "/tmp/employee.ser";

    @After
    public void tearDown() {
        removeFile(FILE_NAME);
    }

    // ref: http://www.tutorialspoint.com/java/java_serialization.htm
    @Test
    public void test_serialization() {
        Employee e1 = new Employee();
        e1.name = "1ambda";
        e1.address = "Pangyo";
        e1.SSN = 10243145;
        e1.number = 10;

        serializeEmployee(e1, FILE_NAME);
        Employee e2 = deserializeEmployee(FILE_NAME);

        assertThat(e2).isNotEqualTo(e1);
        e2.SSN = e1.SSN;
        assertThat(e2).isEqualTo(e1);
    }

    private Employee deserializeEmployee(String filename) {
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        Employee e = null;

        try {

            fis = new FileInputStream(filename);
            ois = new ObjectInputStream(fis);

            e = (Employee) ois.readObject();

        } catch (FileNotFoundException t) {
            t.printStackTrace();
        } catch (IOException t) {
            t.printStackTrace();
        } catch (ClassNotFoundException t) {
            t.printStackTrace();
        } finally {
            IOUtils.closeQuietly(ois);
            IOUtils.closeQuietly(fis);
        }

        return e;
    }

    private void serializeEmployee(Employee e, String filename) {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {

            fos = new FileOutputStream(filename);
            oos = new ObjectOutputStream(fos);

            oos.writeObject(e);

        } catch (IOException t) {
            t.printStackTrace();
        } finally {
            IOUtils.closeQuietly(oos);
            IOUtils.closeQuietly(fos);
        }
    }

    private void removeFile(String filename) {
        try {
            File f = new File(filename);
            f.delete();
        } catch(RuntimeException e) {
            e.printStackTrace();
        }
    }
}
