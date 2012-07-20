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

package com.ijuru.kumva.remote;

import java.net.MalformedURLException;
import java.net.URL;

import junit.framework.TestCase;

/**
 * Test case for Dictionary
 */
public class RemoteDictionaryTest extends TestCase {
	
	private RemoteDictionary dictionary = new RemoteDictionary("http://kinyarwanda.net", "Kinyarwanda.net", "3.0", "rw", "en");

	public void test_createInfoURL() throws MalformedURLException {
		assertEquals(new URL("http://kinyarwanda.net/meta/site.xml.php"), dictionary.createInfoURL());
	}
	
	public void test_createQueryURL() throws MalformedURLException {
		assertEquals(new URL("http://kinyarwanda.net/meta/query.xml.php?q=test&limit=10&entries=1&ref=junit"), dictionary.createQueryURL("test", 10, "junit"));
	}
	
	public void test_createSuggestionsURL() throws MalformedURLException {
		assertEquals(new URL("http://kinyarwanda.net/meta/suggest.php?term=test&format=jquery"), dictionary.createSuggestionsURL("test"));
	}
}
