package com.api.string;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class StringBuilderTest {

	private String str1;
	
	@Before
	public void setup() {
		str1 = "Welcome to Java";
	}
	
	@Test
	public void testAppend() {
		StringBuilder sb = new StringBuilder(str1);
		sb.append(" Programming!");
		
		assertThat(sb.toString(), is("Welcome to Java Programming!"));
		assertThat(sb.length(), is(28));
	}
	
	@Test
	public void testReverse() {
		StringBuilder sb = new StringBuilder("java");
		assertTrue(sb.reverse().toString().equals("avaj"));
	}
}
