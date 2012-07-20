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

package com.ijuru.kumva.xml;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import junit.framework.TestCase;

import com.ijuru.kumva.Entry;
import com.ijuru.kumva.Meaning;
import com.ijuru.kumva.Revision;

/**
 * Test case for EntriesXMLHandler class
 */
public class EntriesXMLHandlerTest extends TestCase {
	
	public void test_withParser() throws ParserConfigurationException, SAXException, IOException {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser parser = factory.newSAXParser();
		
		final List<Entry> entries = new ArrayList<Entry>();
		EntriesXMLHandler handler = new EntriesXMLHandler(new EntryParsedListener() {
			public void entryParsed(Entry entry) {
				entries.add(entry);
			}
		});
			
		// Get embedded XML file
		InputStream stream = EntriesXMLHandlerTest.class.getResourceAsStream("/test-query.xml");
		
		// Run SAX parser
		InputSource source = new InputSource(stream);
		parser.parse(source, handler);
		stream.close();
		
		assertEquals("xtest", handler.getQuery());
		assertEquals("test", handler.getSuggestion());
		assertEquals(3, entries.size());
		assertEquals(1, entries.get(0).getRevisions().size());
		
		Revision revision1 = entries.get(0).getRevisions().get(0);
		assertEquals("n", revision1.getWordClass());
		assertEquals(list(7, 8), revision1.getNounClasses());
		assertEquals("iki", revision1.getPrefix());
		assertEquals("zamini", revision1.getLemma());
		assertEquals("ibi-", revision1.getModifier());
		assertEquals("ikizamini", revision1.getPronunciation());
		
		// Check meanings
		assertEquals(2, revision1.getMeanings().size());
		assertEquals("examination, exam, test, evaluation", revision1.getMeanings().get(0).getText());
		assertEquals(0, revision1.getMeanings().get(0).getFlags());
		assertEquals("just testing", revision1.getMeanings().get(1).getText());
		assertEquals(Meaning.FLAG_RARE|Meaning.FLAG_OLD, revision1.getMeanings().get(1).getFlags());
		assertEquals("This is a comment", revision1.getComment());
		
		// Check form tags
		assertEquals(2, revision1.getTags("form").size());
		assertEquals("rw", revision1.getTags("form").get(0).getLang());
		assertEquals("ikizamini", revision1.getTags("form").get(0).getText());
		assertEquals("rw", revision1.getTags("form").get(1).getLang());
		assertEquals("ibizamini", revision1.getTags("form").get(1).getText());
		
		// Check variant tags
		assertEquals(1, revision1.getTags("variant").size());
		assertEquals("rw", revision1.getTags("variant").get(0).getLang());
		assertEquals("ikizami", revision1.getTags("variant").get(0).getText());
		
		// Check meaning tags
		assertEquals(4, revision1.getTags("meaning").size());
		assertEquals("en", revision1.getTags("meaning").get(0).getLang());
		assertEquals("examination", revision1.getTags("meaning").get(0).getText());
		assertEquals("en", revision1.getTags("meaning").get(1).getLang());
		assertEquals("exam", revision1.getTags("meaning").get(1).getText());
		assertEquals("en", revision1.getTags("meaning").get(2).getLang());
		assertEquals("test", revision1.getTags("meaning").get(2).getText());
		assertEquals("en", revision1.getTags("meaning").get(3).getLang());
		assertEquals("evaluation", revision1.getTags("meaning").get(3).getText());
		
		// Check root tags
		assertEquals(1, revision1.getTags("root").size());
		assertEquals("fr", revision1.getTags("root").get(0).getLang());
		assertEquals("examen", revision1.getTags("root").get(0).getText());
	}
	
	/**
	 * Utility method to make a list of integers
	 * @param integers the integers
	 * @return the list
	 */
	private static List<Integer> list(Integer ... integers) {
		return Arrays.asList(integers);
	}
}
