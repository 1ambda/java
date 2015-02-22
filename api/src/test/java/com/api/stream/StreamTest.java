package com.api.stream;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.junit.Test;

import com.api.java8.Person;

public class StreamTest {

	@Test
	public void test1() {
		
		List<Integer> xs = Arrays.asList(1, 2, 3, 4, 5);
		int squaredSum = xs.stream().map(x -> x * x).mapToInt(x -> x).sum();
		
		assertThat(squaredSum, is(55));
	}
	
	@Test
	public void test2() {
	
		// average() returns OptionDouble type
		double avg = Arrays.stream(new int[] {1, 2, 3}).map(n -> 2*n + 1).average().orElse(0);
		assertThat(avg, is(5.0));
	}
	
	@Test
	public void test3() {
	  List<Person> persons = Arrays.asList(
	      new Person("lambda1", 20, "US"), 
	      new Person("lambda2", 21, "CA"), 
	      new Person("lambda3", 22, "KO"));
	  
	  Stream<Person> ps = persons.stream().filter(p -> p.getAge() >= 21);
	  ps.forEach(p -> assertTrue(p.getAge() >= 21));
	}
}
