package com.api.collection;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import org.junit.Test;

public class ListTest {
  
  @Test
  public void testImpl() {
    // ArrayList, Vector, LinkedList implements List interface
    List<String> ss = new ArrayList<String>();
    List<Integer> ns = new Vector<Integer>();
    List<Double> ds = new LinkedList<Double>();
  }
  

}
