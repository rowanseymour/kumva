/**
 * Copyright 2011 Rowan Seymour
 * 
 * This file is part of Kumva.
 *
 * Kumva is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Kumva is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Kumva. If not, see <http://www.gnu.org/licenses/>.
 */

package com.ijuru.kumva.util;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import com.ijuru.kumva.util.Utils;

/**
 * Test case for Util class
 */
public class UtilsTest  extends TestCase {
	
	public void test_isEmpty() {
		assertTrue(Utils.isEmpty(null));
		assertTrue(Utils.isEmpty(""));
		assertFalse(Utils.isEmpty(" "));
		assertFalse(Utils.isEmpty("x"));
	}
	
	public void test_capitalize() {
		assertEquals("", Utils.capitalize(""));
		assertEquals("Test", Utils.capitalize("test"));
	}
	
	public void test_parseInteger() {
		assertNull(Utils.parseInteger(""));
		assertNull(Utils.parseInteger("x"));
		assertNull(Utils.parseInteger("1.5"));
		assertEquals(new Integer(-123), Utils.parseInteger("-123"));
		assertEquals(new Integer(456), Utils.parseInteger("456"));
	}
	
	public void test_getLanguageName() {
		assertEquals("xx", Utils.getLanguageName("xx"));
		assertEquals("English", Utils.getLanguageName("en"));
		assertEquals("Kinyarwanda", Utils.getLanguageName("rw"));
	}
	
	public void test_makeCSV() {
		List<Integer> arr0 = new ArrayList<Integer>();
		assertEquals("", Utils.makeCSV(arr0));
		
		List<Integer> arr1 = new ArrayList<Integer>();
		arr1.add(new Integer(123));
		assertEquals("123", Utils.makeCSV(arr1));
		
		List<Integer> arr2 = new ArrayList<Integer>();
		arr2.add(new Integer(1));
		arr2.add(new Integer(-2));
		arr2.add(new Integer(3));
		assertEquals("1, -2, 3", Utils.makeCSV(arr2));
	}
	
	public void test_parseCSV() {
		List<String> strs0 = Utils.parseCSV("");
		assertEquals(0, strs0.size());
		
		List<String> strs1 = Utils.parseCSV("abc");
		assertEquals("abc", strs1.get(0));
		assertEquals(1, strs1.size());
		
		List<String> strs2 = Utils.parseCSV("abc, 2,x");
		assertEquals("abc", strs2.get(0));
		assertEquals("2", strs2.get(1));
		assertEquals("x", strs2.get(2));
		assertEquals(3, strs2.size());
	}
	
	public void test_parseCSVIntegers() {
		List<Integer> ints0 = Utils.parseCSVIntegers("");
		assertEquals(0, ints0.size());
		
		List<Integer> ints1 = Utils.parseCSVIntegers("123");
		assertEquals(new Integer(123), ints1.get(0));
		assertEquals(1, ints1.size());
		
		List<Integer> ints2 = Utils.parseCSVIntegers("1, -2,x,3");
		assertEquals(new Integer(1), ints2.get(0));
		assertEquals(new Integer(-2), ints2.get(1));
		assertEquals(new Integer(3), ints2.get(2));
		assertEquals(3, ints2.size());
	}
}
