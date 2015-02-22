package com.api.string;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class StringTest {

	private String str1;

	@Before
	public void init() {
		str1 = "Hello World";
	}
	
	@Test
	public void testLength() {
		assertThat(str1.length() ,is(11));
	}

	@Test
	public void testCharAt() {
		assertThat(str1.charAt(10) , is('d'));
		assertThat(str1.charAt(0) , is('H'));
	}

	@Test
	public void testIndexOf() {
		assertThat(str1.indexOf('o') , is(4));
		assertThat(str1.indexOf("World") , is(6));
		
	}
	
	@Test
	public void testConcat() {
		// String is immutable. So, The concat method requires O(N) time
		String name = " 1ambda";
		assertThat(str1.concat(name) , is("Hello World 1ambda"));
	}
	
	@Test
	public void testReplace() {
		String replaced = str1.replace('o', '0');
		assertThat(replaced , is("Hell0 W0rld"));
	}
	
	@Test
	public void testSubstring() {
		// since Java 1.7.0-06, String's substring costs linear time.
		// see: http://java-performance.info/changes-to-string-java-1-7-0_06/
		
		assertThat(str1.substring(6), is("World"));
		assertThat(str1.substring(2, 5), is("llo"));
	}
	
	@Test 
	public void testIntern() {
		
		/* 
		  *new String objects created using String literal
		 * are maintained in the 'PermGen Space'.
		 */
	
		String s1 = "Scala";
		String s2 = "Scala";
		String s3 = new String("Scala");
	
		
		assertTrue(s1.equals(s2));
		assertTrue(s1.equals(s3));
		assertTrue(s2.equals(s3));
		
		assertTrue(s1 == s2);
		assertFalse(s1 == s3);
		
		// you can put any string into pool by calling intern()
		s3.intern();
	}
}






















