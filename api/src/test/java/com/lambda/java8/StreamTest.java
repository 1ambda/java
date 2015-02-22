package com.lambda.java8;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

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
}
