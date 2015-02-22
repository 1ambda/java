package com.api.object;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

// ref:
// http://howtodoinjava.com/2012/10/09/working-with-hashcode-and-equals-methods-in-java/ 
public class ObjectTest {

  @Test
  public void testEmployee1() {
    // equals should return false since no overrided equals()
    Employee1 e1 = new Employee1(1);
    Employee1 e2 = new Employee1(1);
    
    assertFalse(e1.equals(e2));
  }
  
  @Test
  public void testEmployee2() {
    // Employee2 class overrided only equals method
    Employee2 e1 = new Employee2(1);
    Employee2 e2 = new Employee2(1);
    
    assertTrue(e1.equals(e2));
  }
  
  @Test
  public void testEmployee2HashCode() {
    // test should fail because Employee2 doesn't have overrided hashCode()
    Employee2 e1 = new Employee2(1);
    Employee2 e2 = new Employee2(1);

    Set<Employee2> s = new HashSet<Employee2>();
    s.add(e1);
    s.add(e2);
   
    // s.size == 2
    assertFalse(s.size() == 1);
  }
  
  @Test
  public void testEmployee3HashCode() {
    // test should fail because Employee2 doesn't have overrided hashCode()
    Employee3 e1 = new Employee3(1);
    Employee3 e2 = new Employee3(1);

    Set<Employee3> s = new HashSet<Employee3>();
    s.add(e1);
    s.add(e2);
   
    // s.size == 1
    assertThat(s.size(), is(1));
  }
}
